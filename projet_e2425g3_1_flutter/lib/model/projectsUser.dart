import 'package:projet_e2425g3_1_flutter/model/project.dart';
import 'package:projet_e2425g3_1_flutter/model/user.dart';

class ProjectsUser {
  final User user;
  final List<Project> projects;

  ProjectsUser({required this.user, required this.projects});
  factory ProjectsUser.fromJson(Map<String, dynamic> data) {
    return ProjectsUser(
        user: User.fromJson(data['user']),
        projects: (data['projects'] as List)
            .map((project) => Project.fromJson(project))
            .toList());
  }
  Map<String, dynamic> toJson() {
    return {
      'user': user.toJson(),
      'projects': projects.map((project) => project.toJson()).toList(),
    };
  }
}
