import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Team } from '../../models/team';

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  constructor(private http: HttpClient) { }

  private apiUrl = 'http://localhost:8082/api/project';

  getProjectMembers(projectId: number): Observable<Team[]> {
    return this.http.get<any>(`${this.apiUrl}/${projectId}?id=${projectId}`).pipe(
      map(response => response.users) 
    );
  }

  getAllUsers(): Observable<Team[]>{
    return this.http.get<Team[]>('http://localhost:8082/api/user/all');
    
  }
}
