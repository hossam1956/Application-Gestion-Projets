import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

interface Task {
  id: number;
  title: string;
  description: string;
  status: string;
  date: Date;
}

@Component({
  selector: 'app-tasks-list',
  templateUrl: './tasks-list.component.html',
  styleUrls: ['./tasks-list.component.scss'],
  standalone: true,
  imports: [CommonModule],
 
})
export class TasksListComponent implements OnInit {
  tasks: Task[] = [];
  paginatedTasks: Task[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalPages: number = 0;

  projectId!: number;

  constructor(private route: ActivatedRoute){}
  ngOnInit() {
    this.projectId = +this.route.snapshot.paramMap.get('id')!;
    console.log('Project ID:', this.projectId);

    this.tasks = [
      { id: 1, title: 'Task 1', description: 'Description 1', status: 'To Do', date: new Date() },
      { id: 2, title: 'Task 2', description: 'Description 2', status: 'In Progress', date: new Date() },
      { id: 3, title: 'Task 3', description: 'Description 3', status: 'Completed', date: new Date() },
      { id: 4, title: 'Task 4', description: 'Description 4', status: 'To Do', date: new Date() },
      { id: 5, title: 'Task 5', description: 'Description 5', status: 'In Progress', date: new Date() },
      { id: 6, title: 'Task 6', description: 'Description 6', status: 'Completed', date: new Date() },
      { id: 7, title: 'Task 7', description: 'Description 7', status: 'To Do', date: new Date() },
      { id: 8, title: 'Task 8', description: 'Description 8', status: 'In Progress', date: new Date() },
      { id: 9, title: 'Task 9', description: 'Description 9', status: 'Completed', date: new Date() },
      { id: 10, title: 'Task 10', description: 'Description 10', status: 'To Do', date: new Date() },
    ];

    this.tasks.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());

    this.totalPages = Math.ceil(this.tasks.length / this.itemsPerPage);
    this.updatePaginatedTasks();
  }

  updatePaginatedTasks() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedTasks = this.tasks.slice(startIndex, endIndex);
  }

  changePage(page: number) {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.updatePaginatedTasks();
  }

  get totalPagesArray() {
    return Array(this.totalPages).fill(0).map((_, i) => i + 1);
  }

  getStatusClass(status: string) {
    switch (status) {
      case 'To Do':
        return 'text-primary';
      case 'In Progress':
        return 'text-warning';
      case 'Completed':
        return 'text-success';
      default:
        return 'text-muted';
    }
  }
}
