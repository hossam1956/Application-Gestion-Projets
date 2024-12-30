import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TaskService } from '../../services/task/task.service';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, of, Subscription, switchMap } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { Team } from '../../models/team';
import { TeamService } from '../../services/team/team.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-task',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './add-task.component.html',
  styleUrl: './add-task.component.scss'
})
export class AddTaskComponent implements OnInit{
  projectId!: number;
  private routeSub!: Subscription;

  members : Team[] = [];

  taskForm : FormGroup;
  @Output() taskAdded: EventEmitter<void> = new EventEmitter<void>();

  ngOnInit(): void {
    this.routeSub = this.route.paramMap.subscribe((params) => {
      this.projectId = +params.get('id')!;
    });
    this.getAllMembers();
  }


  constructor(
    private fb: FormBuilder,
    private taskService: TaskService,
    private router: Router,
    private route: ActivatedRoute,
    private teamService : TeamService,
  ) {
    this.taskForm = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      priority: ['', Validators.required],
      deadline: ['', Validators.required],
      userId: ['', Validators.required]
    });
  }
  
  getAllMembers():void{
    this.members = [];
    this.teamService.getProjectMembers(this.projectId).subscribe({
      next: (members) => {
        this.members = members; 
        console.log(members);
      }
    });

  }

  onSubmit(): void {
    if (this.taskForm.valid) {
      const taskData = this.taskForm.value;
      console.log('Task Submitted: ', taskData);
  } else {
      console.log('Form is invalid');
  }
  
    const formValues = this.taskForm.value;
    console.log('Form Values:', formValues);
  
    const title = formValues.title;
    const description = formValues.description;
    const priority = formValues.priority;
    const deadline = formValues.deadline;
    const status = 'to do';
    const userId = formValues.userId;
    console.log('Selected User ID:', userId);
  
    const queryParams = `?title=${encodeURIComponent(title)}&priority=${encodeURIComponent(priority)}&status=${encodeURIComponent(status)}&description=${encodeURIComponent(description)}&deadline=${encodeURIComponent(deadline)}&projectId=${this.projectId}`;
  
    this.taskService.addTask(queryParams).pipe(
      switchMap((response) => {
        console.log('Task added successfully', response);
        const taskId = response.id;
  
        const assignUserQuery = `?userId=${encodeURIComponent(userId)}&taskId=${encodeURIComponent(taskId)}`;
        return this.taskService.assignTaskToUser(assignUserQuery).pipe(
          catchError((error) => {
            console.error('Failed to assign user to task', error);
            return of(null); 
          })
        );
      })
    ).subscribe({
      next: () => {
        console.log('Task added and assignment attempted');
        this.taskService.getAllTasks(this.projectId).subscribe({
          next: (tasks) => {
            console.log('Task list refreshed', tasks); 
            this.taskAdded.emit();
          },
          error: (error) => {
            console.error('Failed to refresh task list', error);
          }
        });
      },
      error: (error) => {
        console.error('Failed to add task', error);
      }
    });
  }
  
  


}
