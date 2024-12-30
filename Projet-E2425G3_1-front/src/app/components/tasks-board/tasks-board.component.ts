import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TaskService } from '../../services/task/task.service';
import { Task } from '../../models/task'; 
import { AddTaskComponent } from '../add-task/add-task.component';
import { Subscription } from 'rxjs';
import { UpdateTaskComponent } from "../update-task/update-task.component";

@Component({
  selector: 'app-tasks-board',
  standalone: true,
  imports: [CommonModule, AddTaskComponent, UpdateTaskComponent],
  templateUrl: './tasks-board.component.html',
  styleUrls: ['./tasks-board.component.scss']
})
export class TasksBoardComponent implements OnInit {


  projectId!: number;
  tasks: Task[] = [];
  sections = ['to do', 'in progress', 'done'];
  draggedTask: Task | null = null;
  private routeSub!: Subscription;


  constructor(
    private route: ActivatedRoute,
    private taskService: TaskService 
  ) {}

  ngOnInit(): void {
    this.routeSub = this.route.paramMap.subscribe((params) => {
      this.projectId = +params.get('id')!;
      this.loadTasks();
    });
  }

  loadTasks(): void {
    this.tasks = [];
  
    this.taskService.getAllTasks(this.projectId).subscribe({
      next: (tasks) => {
        this.tasks = tasks; 
        console.log(tasks);
      },
      error: (err) => {
        console.error('Failed to fetch tasks:', err);
      }
    });
  }


  editClick() {
}
  

  getTasksForSection(section: string): Task[] {
    return this.tasks.filter((task) => task.status.toLowerCase() === section.toLowerCase());
  }
  

  onDragStart(event: DragEvent, task: Task): void {
    this.draggedTask = task;
    event.dataTransfer?.setData('text/plain', JSON.stringify(task));
    event.dataTransfer!.effectAllowed = 'move';
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    event.dataTransfer!.dropEffect = 'move';
  }

  onDrop(event: DragEvent, newStatus: string): void {
    event.preventDefault();
  
    if (!this.draggedTask) {
      return;
    }
  
    const taskIndex = this.tasks.findIndex((t) => t.id === this.draggedTask!.id);
    if (taskIndex !== -1) {
      const updatedTask = { ...this.tasks[taskIndex], status: newStatus };
  
      this.taskService.updateTaskStatus(updatedTask.id, newStatus, this.projectId).subscribe({
        next: (updatedTaskFromApi) => {
          this.tasks[taskIndex] = updatedTaskFromApi;
          console.log(`Task #${updatedTaskFromApi.id} status updated to ${newStatus}`);
        },
        error: (err) => {
          console.error(`Failed to update task #${this.draggedTask!.id}:`, err);
        },
      });
    }
  
    this.draggedTask = null;
  }
  

  copyTaskId(id: number): void {
    navigator.clipboard.writeText(`${id}`).then(() => {
      alert(`Task ID #${id} copied to clipboard!`);
    });
  }

  ngOnDestroy(): void {
    if (this.routeSub) {
      this.routeSub.unsubscribe();
    }
  }
}
