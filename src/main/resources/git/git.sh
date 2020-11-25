make
make dev
make fmt
git status
git diff
git checkout -b tidb3.0 origin/release-3.0-6910eae2a1d9
git checkout release-3.0-6910eae2a1d9
git remote add ti-srebot git@github.com:ti-srebot/tidb.git
git fetch ti-srebot
git remote -v
git commit -m 'resolve conflicts'
git push