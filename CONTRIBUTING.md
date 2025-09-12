# 🤝 Contributing to BubbleTea

Thank you for your interest in contributing to BubbleTea! We welcome contributions from everyone.

## 🌟 Ways to Contribute

### 🐛 Reporting Bugs
- Use the [Bug Report template](.github/ISSUE_TEMPLATE/bug_report.md)
- Search existing issues before creating a new one
- Include detailed steps to reproduce the issue
- Provide device/OS information and app version

### 💡 Suggesting Features
- Use the [Feature Request template](.github/ISSUE_TEMPLATE/feature_request.md)
- Clearly describe the feature and its benefits
- Consider how it fits with the app's goals

### 🔧 Code Contributions
- Fork the repository
- Create a feature branch from `main`
- Follow our coding standards
- Write tests for new functionality
- Update documentation as needed

### 📝 Documentation
- Help improve README, comments, or guides
- Fix typos or unclear instructions
- Add examples or tutorials

## 🚀 Development Setup

### Prerequisites
- Android Studio Arctic Fox or later
- Java 8+
- Android SDK API 34
- Git

### Local Development
1. **Fork and clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/BubbleTea.git
   cd BubbleTea
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Open the project directory
   - Wait for Gradle sync

3. **Build and test**
   ```bash
   ./gradlew clean build
   ./gradlew test
   ```

4. **Run on device/emulator**
   ```bash
   ./gradlew installDebug
   ```

## 📝 Coding Standards

### Kotlin Style
- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Prefer immutable data structures

### Android Guidelines
- Follow [Material Design 3](https://m3.material.io/) principles
- Use Jetpack Compose for UI development
- Follow Android architecture best practices
- Minimize dependencies

### Git Workflow
- Use descriptive commit messages
- Keep commits atomic and focused
- Rebase instead of merge when possible
- Use conventional commit format:
  ```
  type(scope): description
  
  Examples:
  feat(ui): add dark theme toggle
  fix(order): resolve payment calculation bug
  docs(readme): update installation instructions
  ```

## 🧪 Testing

### Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# All tests
./gradlew check
```

### Test Requirements
- Write unit tests for business logic
- Add UI tests for critical user flows
- Maintain test coverage above 80%
- Test on different screen sizes

## 📋 Pull Request Process

1. **Before submitting**
   - Ensure all tests pass
   - Run linting: `./gradlew lint`
   - Update documentation if needed
   - Test on multiple devices/screen sizes

2. **Creating the PR**
   - Use the [PR template](.github/pull_request_template.md)
   - Link related issues with "Fixes #123"
   - Provide clear description of changes
   - Add screenshots for UI changes

3. **Review process**
   - Maintain a respectful and constructive tone
   - Address feedback promptly
   - Keep discussions focused on the code

## 🏷️ Issue Labels

- `bug` - Something isn't working
- `enhancement` - New feature or request
- `documentation` - Improvements or additions to docs
- `good first issue` - Good for newcomers
- `help wanted` - Extra attention is needed
- `question` - Further information is requested

## 💬 Communication

- **Issues**: For bug reports and feature requests
- **Discussions**: For questions and general discussion
- **Email**: For security issues (contact maintainer)

## 🎉 Recognition

Contributors will be:
- Added to the contributors list
- Mentioned in release notes for significant contributions
- Invited to be maintainers for sustained contributions

## ❓ Questions?

Don't hesitate to ask! Create an issue with the `question` label or start a discussion.

Thank you for contributing to BubbleTea! 🧋