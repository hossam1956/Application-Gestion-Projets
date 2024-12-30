import { Component, OnInit} from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Project } from '../../models/project';
import { ProjectService } from '../../services/project/project.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink,RouterLinkActive,CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent implements OnInit {
  constructor(private projectService: ProjectService){}
 
  projects: Project[]= [];
  ngOnInit(): void {
    this.fetchProjects();
  }

  fetchProjects():void{
    this.projectService.getAllProjects().subscribe(
      (data: Project[]) => {
        this.projects= data;
      },
      (error) => {
        console.error('Error fetching projects:', error);
      }
      
    );
  }

openDropdowns: { [key: string]: boolean } = {};

toggleDropdown(projectId: string): void {
  this.openDropdowns[projectId] = !this.openDropdowns[projectId];
}




}
