Const scrWidth = 640
Const scrHeight = 480
Graphics3D scrWidth, scrHeight, 0, 2
AmbientLight 255, 255, 255
AppTitle "3D Lines"
AntiAlias True
SeedRnd MilliSecs()

camDefault = CreateCamera()
CameraClsColor camDefault, 110, 220, 250
MoveEntity camDefault, 0, 5, 0

lgtDefault = CreateLight(2, camDefault)
LightRange lgtDefault, 5
MoveEntity lgtDefault, 0, 3, 0

mdlBall = CreateSphere()
MoveEntity mdlBall, 6, 2, 3
mdlCube = CreateCube()
MoveEntity mdlCube, 4, 3.5, 4
ScaleEntity mdlCube, 3.4, 0.3, 0.3
mdlCylinder = CreateCylinder()
ScaleEntity mdlCylinder, 1, 5, 1
EntityColor mdlCylinder, 255, 200, 50
EntityColor mdlCube, 6, 11, 19
EntityColor mdlBall, 255, 0, 100

mdlGround = CreatePlane(16)
EntityColor mdlGround, 50, 150, 50

Global FrameStart
Global DeltaTime# = 0.1
Global GameTime# = 0
Global FPS = 1200
Global FPSCount = FPS
Global timFPS

fntDefault = LoadFont("FixedSys", 24)
SetFont fntDefault

ln = CreateLine(0, 10, 10, 8, 5, 10, camDefault)

CreateLine(2, 2, 2, 2, 2, 4, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)
CreateLine(2, 2, 4, 4, 2, 4, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)
CreateLine(4, 2, 4, 4, 2, 2, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)
CreateLine(4, 2, 2, 2, 2, 2, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)

CreateLine(2, 2, 2, 2, 4, 2, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)
CreateLine(2, 2, 4, 2, 4, 4, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)
CreateLine(4, 2, 4, 4, 4, 4, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)
CreateLine(4, 2, 2, 4, 4, 2, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)

CreateLine(2, 4, 2, 2, 4, 4, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)
CreateLine(2, 4, 4, 4, 4, 4, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)
CreateLine(4, 4, 4, 4, 4, 2, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)
CreateLine(4, 4, 2, 2, 4, 2, camDefault, 255, 255, 255, 1, 1, 1, 1, -1)

For i = 0 To 100
	CreateLine(Rnd(0, 30) - 15, Rnd(0, 10), Rnd(0, 30) - 15, Rnd(0, 30) - 15, Rnd(0, 10), Rnd(0, 30) - 15, camDefault, Rand(0, 255), Rand(0, 255), Rand(0, 255), Rnd(0.2, 1), 1)
Next


While Not KeyDown(1)
	
	; Frame Update
	FrameStart = MilliSecs()
	If timFPS < FrameStart Then
		FPS = FPSCount
		FPSCount = 0
		timFPS = FrameStart + 1000
		DeltaTime = 1.0 / Float(FPS)
		GameTime = GameTime + DeltaTime
	End If
	
	; Movement
	mx# = KeyDown(203) - KeyDown(205)
	mx = mx * DeltaTime * 50
	mz# = KeyDown(200) - KeyDown(208)
	mz = mz * DeltaTime * 3
	MoveEntity camDefault, 0, 0, mz
	TurnEntity camDefault, 0, mx, 0
	
	UpdateLines()
	
	; 3D Drawing
;	SetBuffer BackBuffer()
	Cls
	RenderWorld
	
	Text 1, 0, "FPS: " + FPS
	
	Flip False
	
	FPSCount = FPSCount + 1
	
Wend

Type Line3D
	Field x#[1], y#[1], z#[1]
	Field cam
	Field thick#
	Field mesh, sfc
	Field vertex
	Field pvt[2]
End Type

Function CreateLine(x1#, y1#, z1#, x2#, y2#, z2#, cam, r = 255, g = 255, b = 255, a# = 1, thick = 1, fx = 1, blend = 1, order = 0, zoom# = 1)
	Local l.Line3D = New Line3D
	l\x[0] = x1
	l\y[0] = y1
	l\z[0] = z1
	l\x[1] = x2
	l\y[1] = y2
	l\z[1] = z2
	l\cam = cam
	l\thick = Float(thick) / zoom
	l\mesh = CreateMesh()
	l\sfc = CreateSurface(l\mesh)
	EntityColor l\mesh, r, g, b
	EntityAlpha l\mesh, a
	EntityOrder l\mesh, order
	EntityFX l\mesh, fx
	EntityBlend l\mesh, blend
	l\vertex = AddVertex(l\sfc, x1, y1, z1)
	AddVertex(l\sfc, x2, y2, z2)
	AddVertex(l\sfc, x2, y2, z2)
	AddVertex(l\sfc, x1, y1, z1)
	AddTriangle(l\sfc, l\vertex, l\vertex+1, l\vertex+2)
	AddTriangle(l\sfc, l\vertex, l\vertex+2, l\vertex+3)
	AddTriangle(l\sfc, l\vertex+2, l\vertex+1, l\vertex)
	AddTriangle(l\sfc, l\vertex+3, l\vertex+2, l\vertex)
	AddTriangle(l\sfc, l\vertex+3, l\vertex, l\vertex+1)
	AddTriangle(l\sfc, l\vertex+3, l\vertex+1, l\vertex+2)
	l\pvt[0] = CreatePivot()
	PositionEntity l\pvt[0], x1, y1, z1
	l\pvt[1] = CreatePivot()
	PositionEntity l\pvt[1], x2, y2, z2
	l\pvt[2] = CreatePivot()
	Return Handle(l)
End Function

Function ChangeLine(ln, x1#, y1#, z1#, x2#, y2#, z2#, cam, r = 255, g = 255, b = 255, a# = 1, thick = 1, fx = 1, blend = 1, order = 0, zoom# = 1)
	Local l.Line3D = Object.Line3D(ln)
	l\x[0] = x1
	l\y[0] = y1
	l\z[0] = z1
	l\x[1] = x2
	l\y[1] = y2
	l\z[1] = z2
	l\cam = cam
	l\thick = Float(thick) / zoom
	EntityColor l\mesh, r, g, b
	EntityAlpha l\mesh, a
	EntityOrder l\mesh, order
	EntityFX l\mesh, fx
	EntityBlend l\mesh, blend
	PositionEntity l\pvt[0], x1, y1, z1
	PositionEntity l\pvt[1], x2, y2, z2
End Function

Function DeleteLine(ln)
	Local l.Line3D = Object.Line3D(ln)
	FreeEntity l\mesh
	Delete l
End Function

Function UpdateLines()
	Local x[1], y[1], r#, d#
	For l.Line3D = Each Line3D
		
		PositionEntity l\pvt[2], l\x[0], l\y[0], l\z[0]
		PointEntity l\pvt[2], l\pvt[1]
		r = VectorYaw(l\x[0] - EntityX(l\cam), 0, l\z[0] - EntityZ(l\cam))
		RotateEntity l\pvt[2], EntityPitch(l\pvt[2]), r-90, 0
		d = EntityDistance(l\cam, l\pvt[2]) * l\thick * 0.00125
		MoveEntity l\pvt[2], 0, -d, 0
		VertexCoords l\sfc, l\vertex, EntityX(l\pvt[2]), EntityY(l\pvt[2]), EntityZ(l\pvt[2])
		MoveEntity l\pvt[2], 0, d*2, 0
		VertexCoords l\sfc, l\vertex+3, EntityX(l\pvt[2]), EntityY(l\pvt[2]), EntityZ(l\pvt[2])
		
		PositionEntity l\pvt[2], l\x[1], l\y[1], l\z[1]
		PointEntity l\pvt[2], l\pvt[0]
		r = VectorYaw(l\x[1] - EntityX(l\cam), 0, l\z[1] - EntityZ(l\cam))
		RotateEntity l\pvt[2], EntityPitch(l\pvt[2]), r+90, 0
		d = EntityDistance(l\cam, l\pvt[2]) * l\thick * 0.00125
		MoveEntity l\pvt[2], 0, -d, 0
		VertexCoords l\sfc, l\vertex+1, EntityX(l\pvt[2]), EntityY(l\pvt[2]), EntityZ(l\pvt[2])
		MoveEntity l\pvt[2], 0, d*2, 0
		VertexCoords l\sfc, l\vertex+2, EntityX(l\pvt[2]), EntityY(l\pvt[2]), EntityZ(l\pvt[2])
		
	Next
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D