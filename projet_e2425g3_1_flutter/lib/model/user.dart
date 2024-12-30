class User {
  final int id;
  final String username;
  final String email;
  final String profileImageUrl;
   User({
    required this.id,
    required this.username,
    required this.email,
    required this.profileImageUrl,
  });
  factory User.fromJson(Map<String, dynamic> data) {
    return User(
      id: data['id'] as int,
      username: data['username'] as String,
      email: data['email'] as String,
      profileImageUrl: data['profileImageUrl'] as String,
    );
  }
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'username': username,
      'email': email,
      'profileImageUrl': profileImageUrl,
    };
  }
}
