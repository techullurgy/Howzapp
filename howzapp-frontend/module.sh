#!/bin/bash

# Root namespace
rootNamespace="com.techullurgy.howzapp"

# Check argument
if [ -z "$1" ]; then
    echo "Usage: $0 module.path.structure"
    echo "Example: $0 core.navigation.data"
    exit 1
fi

# Input module path
modulePath="$1"

# Convert dot notation to folder path
moduleFolder=$(echo "$modulePath" | tr '.' '/')

# Create module folder
mkdir -p "./$moduleFolder"

echo "Created module folder: ./$moduleFolder"


# Create empty build.gradle.kts
touch "./$moduleFolder/build.gradle.kts"

echo "Created build.gradle.kts"


# Convert namespace to folder structure
namespacePath=$(echo "$rootNamespace" | tr '.' '/')

# Create Kotlin source set path
sourcePath="./$moduleFolder/src/commonMain/kotlin/$namespacePath/$moduleFolder"

mkdir -p "$sourcePath"

echo "Created source set:"
echo "$sourcePath"


echo "Module created successfully"