import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Task } from '../../models/task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private apiUrl = 'http://localhost:8082/api/task';

  constructor(private http: HttpClient) { }

  getAllTasks(id: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/project/${id}`)
  }

  addTask(queryParams: string): Observable<any> {
    const url = `${this.apiUrl}${queryParams}`;
    return this.http.post(url, null);
  }

  assignTaskToUser(queryParams: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/addUser${queryParams}`, null); 
  }

  updateTaskStatus(taskId: number, status: string, projectId: number): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${taskId}`, null, {
      params: { status, projectId: projectId.toString() },
    });
  }
  
  

}
