The basic idea here is to illustrate a gemfire gfsh session as slides using reveal.js

Here's an example:

http://eitansuez.github.io/gfsh-reveal/

There's a groovy script (see gfsh-reveal.groovy) that will take as input the output from a gfsh session and marry it with a reveal.js template to produce such pages automatically.

Each slide is basically two fragments, the first is the command and the second is the output.

Invoke script as follows:

    ./gfsh-reveal.groovy gfsh-session

And this will produce `gfsh-session.html`, a slide deck similar to what you see at the above link (sans the h3 headers on each slide, which were added manually).

