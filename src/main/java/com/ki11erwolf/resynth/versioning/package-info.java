/**
 * Provides an alternate versioning manager (online version checker)
 * to the default forge provided one.
 *
 * This one aims to be more extendable, fail-tolerant,
 * configurable, idiot-proof and easier to use.
 */
package com.ki11erwolf.resynth.versioning;

/*
This is an informal specification of the version.json file and interpreter
rules and regulations.

version.json specification:

example of complete version.json:

{
    //Method 1: Quick and Easy
    //Not required if using either of the below two methods
    //Note: It is preferred to use method two or three for completeness though
    "latest": "1.0.1"
    //Not required but recommended (lol).
    "recommended": "1.0.0"
}
//////////////////////////////////////////
{
    //Method 2: Standard
    //Should be perfectly acceptable in most if not all situations
    "latest": {
        "all": "1.0.1",
        "1.12.2": "1.0.1",
        "1.10.2": "1.3.8"
    },
    "recommended": {
        "all": "1.0.0",
        "1.12.2": "1.0.0",
        "1.10.2": "1.2.9"
    }
}
/////////////////////////////////////////
{
    //Method 3: Specific
    //Completely optional if either of the above two methods are present in the file.
    //Allows multiple mods to be configured in the same file.
	"modid": {
		"latest": "1.0.1",
		"recommended": "1.0.0",

		"1.12.2": {
			"latest": "1.0.1",
			"recommended": "1.0.0",

			"versions": [
				"1.0.0-Beta", "1.0.1-Beta", "1.0.0", "1.0.1"
			]
		},
		"1.10.2": {
			"latest": "1.3.8",
			"recommended": "1.2.9",

			"versions": [
				"1.2.1-Beta", "1.2.2-Beta", "1.2.0", "1.2.1"
			]
		}
	},

	//Always Optional
    //Global version update manager disable
    "enable": false
}

The interpreter is defined as the whole versioning system as well
as api, logic and parsing systems.

Rules and regulations:
(all possible regulations are adhered to)
(all rules are adhered to)
1. All keys should be lowercase.
2. Case should be ignored by the interpreter.
3. Json should be compatible with existing forge version.json (except changelog)
4. Json should be able to be ADDED to existing forge version.json
   without conflict.
5. Interpreter should work with with minimum amount of json (method 1)
6. Interpreter should with with all methods.
7. Multiple mod version json objects should be able to fit in the same file using
   method 3.
8. Interpreter should accept and ignore comments.
9. Interpreter should be configurable:
    - Interpreter should be able to be completely turned off (by configuration is a must)
    - All interpreter sub-function (non-vital) should be able to be enabled/disabled (turned on and off)
10. Interpreter should be aware of attacks and avoid them as best possible.
11. Interpreter should not cause fatal errors.
12. INTERPRETER CAN *NEVER* AUTO UPDATE NOR CAN IT DOWNLOAD/INSTALL FILES FILES.
13. Interpreter should be opt-in (must be opt-in though code. Can be on by default in configs.)
14. Interpreter should provide configuration options.
15. The version.json file can be named ANYTHING. (By convention, the version.json
    filename and file content is in entirely lowercase. NOT REQUIRED THOUGH.)
16. Interpreter should use method 3 over method 2 when possible, and method 2 over
    1 when possible.
17. If no versioning information exists within the file (parsable or not),
    the interpreter should give a warning and only fail within controlled and exception
    catching classes.
18. NORMAL use of the versioning system should NEVER cause a crash.
19. The interpreter should give notify of any encountered errors and warnings.
20. Interpreter should be able to be disabled (as off as possible) thought the
    version.json file.
 */