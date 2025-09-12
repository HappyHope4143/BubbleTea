#!/bin/bash

echo "=== BubbleTea View to Compose Migration Verification ==="
echo

echo "✅ 1. Checking MainActivity migration:"
if grep -q "ComponentActivity" app/src/main/java/com/happyhope/bubbletea/MainActivity.kt; then
    echo "  ✓ MainActivity now extends ComponentActivity (Compose)"
else
    echo "  ✗ MainActivity still uses AppCompatActivity (View system)"
fi

if grep -q "setContent" app/src/main/java/com/happyhope/bubbletea/MainActivity.kt; then
    echo "  ✓ MainActivity uses setContent{} (Compose)"
else
    echo "  ✗ MainActivity still uses setContentView() (View system)"
fi

if grep -q "@Composable" app/src/main/java/com/happyhope/bubbletea/MainActivity.kt; then
    echo "  ✓ Contains @Composable functions"
else
    echo "  ✗ No @Composable functions found"
fi

echo

echo "✅ 2. Checking build configuration:"
if grep -q "buildFeatures" app/build.gradle && grep -q "compose true" app/build.gradle; then
    echo "  ✓ Compose build features enabled"
else
    echo "  ✗ Compose build features not enabled"
fi

if grep -q "androidx.compose" app/build.gradle; then
    echo "  ✓ Compose dependencies added"
else
    echo "  ✗ Compose dependencies not found"
fi

if grep -q "activity-compose" app/build.gradle; then
    echo "  ✓ Activity Compose dependency added"
else
    echo "  ✗ Activity Compose dependency not found"
fi

echo

echo "✅ 3. Checking Compose theme setup:"
if [ -f "app/src/main/java/com/happyhope/bubbletea/ui/theme/Theme.kt" ]; then
    echo "  ✓ Compose Theme.kt created"
else
    echo "  ✗ Compose Theme.kt not found"
fi

if [ -f "app/src/main/java/com/happyhope/bubbletea/ui/theme/Color.kt" ]; then
    echo "  ✓ Compose Color.kt created"
else
    echo "  ✗ Compose Color.kt not found"
fi

if [ -f "app/src/main/java/com/happyhope/bubbletea/ui/theme/Type.kt" ]; then
    echo "  ✓ Compose Type.kt created"
else
    echo "  ✗ Compose Type.kt not found"
fi

echo

echo "✅ 4. Checking XML layout removal:"
if [ ! -f "app/src/main/res/layout/activity_main.xml" ]; then
    echo "  ✓ XML layout file removed (activity_main.xml)"
else
    echo "  ✗ XML layout file still exists"
fi

echo

echo "✅ 5. Code comparison:"
echo "  Before (View System):"
echo "    - MainActivity extends AppCompatActivity"
echo "    - Uses setContentView(R.layout.activity_main)"
echo "    - XML layout with ConstraintLayout and TextViews"
echo "    - Traditional View-based UI"
echo
echo "  After (Compose):"
echo "    - MainActivity extends ComponentActivity"
echo "    - Uses setContent { BubbleTeaTheme { ... } }"
echo "    - @Composable WelcomeScreen() function"
echo "    - Declarative UI with Material 3 theming"

echo

if grep -q "ComponentActivity" app/src/main/java/com/happyhope/bubbletea/MainActivity.kt && \
   grep -q "setContent" app/src/main/java/com/happyhope/bubbletea/MainActivity.kt && \
   grep -q "androidx.compose" app/build.gradle && \
   [ ! -f "app/src/main/res/layout/activity_main.xml" ]; then
    echo "🎉 MIGRATION SUCCESSFUL: View system → Jetpack Compose"
else
    echo "❌ MIGRATION INCOMPLETE: Please check the issues above"
fi

echo
echo "=== Migration Complete ==="