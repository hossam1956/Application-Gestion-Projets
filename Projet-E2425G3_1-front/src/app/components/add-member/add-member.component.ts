import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Team } from '../../models/team';
import { TeamService } from '../../services/team/team.service';
import { CommonModule, NgClass } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, NgModel, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { ProjectService } from '../../services/project/project.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-add-member',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,FormsModule],
  templateUrl: './add-member.component.html',
  styleUrl: './add-member.component.scss'
})
export class AddMemberComponent implements OnInit{
  @Input() projectId!: number;
  @Output() memberAdded: EventEmitter<void> = new EventEmitter<void>();

  members: Team[] = [];
  selectedUserId!: number;
  teamForm: FormGroup;


  constructor(private teamService: TeamService, private route: ActivatedRoute,private fb: FormBuilder,
    private projectService: ProjectService
  ){
    this.teamForm = this.fb.group({userId:  ['', Validators.required]});
  }

  ngOnInit(): void {
    this.fetchAllUsers();
  }

  fetchAllUsers(): void {
    this.teamService.getAllUsers().subscribe({
      next: (data) => {
        console.log('Fetched users:', data); 
        this.members = data; 
      },
      error: (err) => {
        console.error('Error fetching users:', err);
      }
    });
  }

  onUserChange(event: any): void {
    this.selectedUserId = parseInt(event.target.value, 10);
    console.log('Selected User ID:', this.selectedUserId);
  }
  
  onSubmit():void {
    if(this.teamForm.invalid){
      return;
    }
    const userId = this.teamForm.value.userId;
    const queryParams = `?userId=${encodeURIComponent(userId)}&projectId=${encodeURIComponent(this.projectId)}`;
    console.log('Formatted query params:', queryParams);

    this.projectService.addUserToProject(queryParams).subscribe({
      next: (response) => {
        console.log('Project added successfully', response);
        this.teamService.getProjectMembers(this.projectId);
        this.memberAdded.emit();
      },
      error: (error: HttpErrorResponse) => {
        console.error('There was an error!', error);
      }
    });
  }
}
