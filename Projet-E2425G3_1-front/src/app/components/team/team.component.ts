import { Component, OnInit, Output } from '@angular/core';
import { AddMemberComponent } from '../add-member/add-member.component';
import { ActivatedRoute } from '@angular/router';
import { Team } from '../../models/team';
import { Subscription } from 'rxjs';
import { TeamService } from '../../services/team/team.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-team',
  standalone: true,
  imports: [AddMemberComponent,CommonModule],
  templateUrl: './team.component.html',
  styleUrl: './team.component.scss'
})
export class TeamComponent implements OnInit{
  @Output() projectId!: number;
  members: Team[]= [];
  private routeSub!: Subscription;

  
  constructor(private route: ActivatedRoute, private teamService: TeamService) {}

  ngOnInit(): void {
    this.routeSub = this.route.paramMap.subscribe((params) => {
      this.projectId = +params.get('id')!;
      this.loadProjectMembers();
    });
  }

  loadProjectMembers(): void {
    this.members = [];
    this.teamService.getProjectMembers(this.projectId).subscribe({
      next: (members) => {
        this.members = members;
      },
      error: (err) => {
        console.error('Failed to fetch project members:', err);
      }
    });
  }
  
  ngOnDestroy(): void {
    if (this.routeSub) {
      this.routeSub.unsubscribe();
    }
  }
}
