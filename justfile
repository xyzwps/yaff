
@test:
    ./mvnw clean test

@git-remote:
    git remote add origin git@gitee.com:xyzwps/yaff.git
    git remote add github git@github.com:xyzwps/yaff.git

@push:
    git push -u origin "$(git branch --show-current)"
    git push -u github "$(git branch --show-current)"