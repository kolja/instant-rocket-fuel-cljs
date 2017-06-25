[![Build Status](https://travis-ci.org/kolja/instant-rocket-fuel-cljs.svg?branch=master)](https://travis-ci.org/kolja/instant-rocket-fuel-cljs)

# Instant Rocket Fuel for ClojureScript

This is a work in progress of porting [Instant Rocket Fuel](https://github.com/kolja/Instant-Rocket-Fuel) to ClojureScript (see the orginal irf's [documentation](http://kolja.github.io/Instant-Rocket-Fuel/)).

## development

Start a REPL with Figwheel:
```
rlwrap lein figwheel repl
```
Start a node Server for the REPL to connect to:
```
node resources/public/js/out/irf.js
```
Run the tests:
```
lein doo node test
```
## License

See the [LICENSE](LICENSE.md) (MIT).
