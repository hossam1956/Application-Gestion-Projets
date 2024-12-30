export interface Task {
    id: number;
    title: string;
    priority: string;
    status: string;
    description: string;
    deadline: Date;
    projectId: number;
    userAssignee: {
      id: number;
      username: string;
      email: string;
      profileImageUrl: string;
    };
  }
  