
stateclass["jumpnrun"] = class StateJumpNRun extends State
  constructor: (@parent) ->
    
    jumpnrunSprite = new Sprite
      "texture": "assets/images/jumpnrun.png"
      "width": 100
      "height": 100
      "innerWidth": 95
      "innerHeight": 95
      "key":
        "00": 0
        "11": 1
        '22': 2
        "33": 3
        "44": 4
        '55': 5
        "66": 6
        "77": 7
        '88': 8
        "99": 9
        "aa": 10
        'bb': 11

    @background = new Map
      "map": "assets/jumpnrun_map.png"
      "pattern": "simple"
      "sprite": jumpnrunSprite
    
    @spaceships = []
    for i in [0..3]
      @spaceships[i] = new Spaceship
    
  update: (delta) ->
    for spaceship in @spaceships
      spaceship.update delta
    
  render: (ctx) -> 
    @background.render(ctx)
    for spaceship in @spaceships
      spaceship.render ctx