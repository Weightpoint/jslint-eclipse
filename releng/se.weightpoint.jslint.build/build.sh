#!/bin/bash

MVN=$HOME/bin/mvn
BUILD_TARGET_DIR=/tmp/jshint-eclipse

# path that are relevant for effective commit
includePaths="bundles/se.weightpoint.jslint bundles/se.weightpoint.jslint.ui releng/se.weightpoint.jslint.feature"

# make sure we're in the git repository root
GIT_ROOT="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && cd ../../ && pwd )"
cd $GIT_ROOT
if [ ! -d ".git" ]; then
  echo "git root directory not found"
  exit 1
fi

# determine last commit
stat="$(git status -s)"
if [ -z "$stat" ]; then
  echo "Find latest commit date in paths:"
  for path in $includePaths; do
    echo "* $path"
  done
  commit_hash=$(git log -1 --format='%h' -- $includePaths)
  commit_subject=$(git log -1 --format='%s' -- $includePaths)
  commit_date=$(date -u -d "$(git log -1 --format='%ci' -- $includePaths)" +"%Y%m%d-%H%M")
  echo "-> $commit_date $commit_subject [$commit_hash]"
else
  echo "Uncommitted changes, run 'git status'"
fi

cd "releng/se.weightpoint.jslint.build" || exit 1

$MVN clean install || exit 1

if [ -n "$commit_hash" ]; then
  # copy resulting repository
  feature_version=`ls -1 repository/target/repository/features/*.jar | sed -e 's/.*_\([0-9\.-]*\)\.jar/\1/'`
  if [ -z "$feature_version" ]; then
    echo "Could not determine feature version"
    exit 1
  fi
  version="$feature_version-$commit_hash"
  echo "Version: $version"
  
  mkdir -p $BUILD_TARGET_DIR
  rsync -av repository/target/repository/ $BUILD_TARGET_DIR/jshint-eclipse-$version
  cp repository/target/se.weightpoint.jslint.repository-*.zip $BUILD_TARGET_DIR/jshint-eclipse-$version.zip
fi

