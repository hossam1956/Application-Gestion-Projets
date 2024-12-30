class Project {
  final int id;
  final int managerId;
  final String nom;
  final DateTime datedebut;
  final DateTime datefin;

  Project({
    required this.id,
    required this.managerId,
    required this.nom,
    required this.datedebut,
    required this.datefin,
  });

  

  factory Project.fromJson(Map<String, dynamic> data) {
    return Project(
        id: data['id'] as int,
        managerId: data['managerId'] as int,
        nom: data['nom'] as String,
        datedebut: DateTime.parse('${data['datedebut']}T00:00:00'),
        datefin: DateTime.parse('${data['datefin']}T00:00:00'));
  }
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'managerId': managerId,
      'nom': nom,
      'datedebut': datedebut.toIso8601String().split('T')[0],
      'datefin': datefin.toIso8601String().split('T')[0],
    };
  }
}
