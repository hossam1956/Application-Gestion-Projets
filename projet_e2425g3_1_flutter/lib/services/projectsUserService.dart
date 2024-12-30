import 'dart:convert';

import 'package:projet_e2425g3_1_flutter/model/project.dart';
import 'package:projet_e2425g3_1_flutter/model/projectsUser.dart';
import 'package:http/http.dart' as http;

class ProjectsUserService {
  static const String baseUrl = 'http://localhost:8082/api';
  Future<List<Project>> fetchProjectOfUser(int userId) async {
    final uri = Uri.parse("$baseUrl/user/$userId")
        .replace(queryParameters: {'id': userId.toString()});
    try {
      final response = await http.get(uri);
      if (response.statusCode == 200) {
        final Map<String, dynamic> jsonData = jsonDecode(response.body);
        ProjectsUser projectsUser = ProjectsUser.fromJson(jsonData);
        return projectsUser.projects;
      } else {
        throw Exception('Failed to fetch Project of connected User');
      }
    } catch (e) {
      rethrow;
    }
  }

  Future<bool> createProject({
    required int idManager,
    required String name,
    required String startDate,
    required String endDate,
  }) async {
    final uri = Uri.parse("$baseUrl/project")
      .replace(queryParameters: {
        'idManager': idManager.toString(),
        'name': name,
        'startDate': startDate,
        'endDate': endDate
      });
    try {
      final response = await http.post(uri);
      if (response.statusCode == 200) {
        return true;
      } else {
        return false;
      }
    } catch (e) {
      throw Exception('Failed to add new Project => Error : $e');
    }
  }
}
