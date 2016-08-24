# pycljs

This is a really simple demo of working with a python + flask backend and
a clojurescript + reagent frontend, with a figwheel workflow.

## dev workflow

It appears that you can use flask with figwheel without much problem using
off-the-shelf workflows, if you're willing to accept some "extra" stuff that
figwheel is doing...

### step 1: start figwheel

It's best to start figwheel first, since the python server isn't going
to work at all until you've got compiled javascript, which is one of the things
figwheel takes care of. So run
```
lein fighweel
```
from the project root.

When you run this, all your clojurescript gets compiled. Furthermore,
figwheel
  * starts a dummy server on 3449 to serve up your index and other assets
  * starts a websockets connection that browsers will connect to for a
    clojurescript repl

Once figwheel is ready, it will pop open a browser window on port 3449.

You'll notice that the heading which we expected to change to say hello from
flask doesn't change; if you open the dev console, you'll see a 404 error when
it tries to hit the `/newtext` endpoint. This is because the figwheel asset
server doesn't have any json endpoints.

### step 2: start the flask app

In a new terminal, run
```
python app/server.py
```
You'll see all the usual flask logging from your backend.

This app, importantly, knows to serve any path that doesn't match some
other route by looking in `../resources/public`, relative to the `server.py`
file. Without this, the css and all of the javascript loads would fail.

### step 3: open in the browser and start work

Go to `localhost:5000` in the browser.

This time, you should see the hello from flask message come up, which is
how we know that the flask backend is working properly.

If you make some edits to clojurescript and save them (make a syntax error,
it's more fun that way), you'll see that the figwheel auto-compile and
auto-reload is working as it should.

### Bonus step: what is figwheel doing?

How did this work? We just stood up two different backend servers, one on 5000
and one on 3449, and connected two browser windows to them. But fighweel seemed
to connect to both of them just fine.

What it actually appears to be doing is broadcasting reloads and also repl
commands to all clients. You can test this in very rough terms by running - with
both clients connected - `(js/alert "hi")`. You'll probaly get an alert popping
up in *both* browser windows.

# notes on making the project

To start off with a figwheel + reagent template, I just ran
`lein new fighweel pycljs -- --reagent`. Then, I made an `app` folder
for my python backend and copied in some boilderplate from a previous
flask app I'd written.

# notes on building a release

I haven't tried it yet, but I think to get a release going, all you
need to do in theory is `lein do clean, cljsbuild once min`.

## License

Copyright © 2016 Steven Troxler

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
