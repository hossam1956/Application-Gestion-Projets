import { Component, OnInit } from '@angular/core';
import { SidebarComponent } from "./shared/sidebar/sidebar.component";
import { PageContentComponent } from "./shared/page-content/page-content.component";
import { ProjectService } from './services/project/project.service';
import { Project } from './models/project';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [SidebarComponent, PageContentComponent, ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'project';

  projects: Project[] = [];

  constructor(private projectService: ProjectService) {}

  ngOnInit(): void {
    this.projectService.projects$.subscribe((projects) => {
      this.projects = projects;
    });

    this.projectService.getAllProjects();
  }

 



}
