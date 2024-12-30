import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProjectService } from '../../services/project/project.service'; 
import { Project } from '../../models/project'; 
import { HttpErrorResponse } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
@Component({
  selector: 'app-add-project',
  standalone: true,
  imports: [ReactiveFormsModule,],
  templateUrl: './add-project.component.html',
  styleUrl: './add-project.component.scss'
})
export class AddProjectComponent {
  projectForm: FormGroup;
   


  constructor(
    private fb: FormBuilder,
    private projectService: ProjectService,
    private router: Router 
  ) {
    this.projectForm = this.fb.group({
      nom: ['', Validators.required],
      datedebut: ['', Validators.required],
      datefin: ['', Validators.required]
    });
  }

  
  onSubmit(): void {
    if (this.projectForm.invalid) {
      return;
    }
  
    console.log(this.projectForm.value);
  
    const formatDate = (date: string) => {
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      const hours = String(d.getHours()).padStart(2, '0');
      const minutes = String(d.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    };
  
    const startDate = formatDate(this.projectForm.value.datedebut);
    const endDate = formatDate(this.projectForm.value.datefin);
    const name = this.projectForm.value.nom;
  
    const queryParams = `?idManager=1&name=${encodeURIComponent(name)}&startDate=${encodeURIComponent(startDate)}&endDate=${encodeURIComponent(endDate)}`;
  
    console.log('Formatted query params:', queryParams);
  
    this.projectService.addProject(queryParams).subscribe({
      next: (response) => {
        console.log('Project added successfully', response);

        this.projectService.getAllProjects();

       
        this.router.navigateByUrl('/').then(() => {
          window.location.reload(); 
        });
      },
      error: (error: HttpErrorResponse) => {
        console.error('There was an error!', error);
      }
    });
  }
  

}