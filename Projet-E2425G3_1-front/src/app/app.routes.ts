import { Routes } from '@angular/router';
import { TestComponent } from './test/test/test.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AddTaskComponent } from './components/add-task/add-task.component';
import { TeamComponent } from './components/team/team.component';
import { AddProjectComponent } from './components/add-project/add-project.component';
import { TasksBoardComponent } from './components/tasks-board/tasks-board.component';
import { TasksListComponent } from './components/tasks-list/tasks-list.component';
import { UpdateTaskComponent } from './components/update-task/update-task.component'
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
export const routes: Routes = [
    {path: 'test', component: TestComponent},
    {path: '', component: DashboardComponent},
    {path: 'add-task',component: AddTaskComponent},
    {path: 'add-project', component: AddProjectComponent },
    { path: 'project/:id/board', component: TasksBoardComponent },
    { path: 'project/:id/tasks-list', component: TasksListComponent },
    { path: 'project/:id/team', component: TeamComponent },

    {path: 'update-task', component: UpdateTaskComponent},
    {path:'login', component:LoginComponent},
    {path:'register', component:RegisterComponent},
    // {path: 'url', compnent: Component Name},
];
