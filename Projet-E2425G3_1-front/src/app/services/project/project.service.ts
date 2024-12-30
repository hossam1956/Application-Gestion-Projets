import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable,map } from 'rxjs';
import { Project } from '../../models/project';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = 'http://localhost:8082/api/user/{id}?id=1';
  private projectsSubject = new BehaviorSubject<Project[]>([]); 
  public projects$ = this.projectsSubject.asObservable();

  constructor(private http: HttpClient) {}

  getAllProjects(): Observable<Project[]> {
    return this.http.get<{ projects: Project[] }>(this.apiUrl).pipe(
      map(response => response.projects) 
    );
  }

  addProject(queryParams: string): Observable<any> {
    const baseUrl = `http://localhost:8082/api/project${queryParams}`;
    console.log('Sending POST request to:', baseUrl);
    return this.http.post(baseUrl, null); 
  }

  addUserToProject(queryParams : string): Observable<any>{
    const baseUrl = `http://localhost:8082/api/project/addUser${queryParams}`;
    console.log('Sending POST request to:', baseUrl);
    return this.http.post(baseUrl, null); 

  }

}
