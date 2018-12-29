README
======

This library is a kotlin parser for the Dice notation with some additions.

Dice notation
-------------

Dice notation is a way to represent dice launches in tabletop games and RPGs. Let's see an example:

```
1d100 - 30
```

Features (to be) Supported
------------------

- As parsed
    - Any dice described as in the standard notation XdY
    - Basic arithmetic operations (+-*/)
    - Constants
    - Reference to variable given as part of the roll
- In the software
    - See the individual result of each dice
    - Success definition with constraints
    - Giving a dictionary of values to reuse a single line
