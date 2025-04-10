<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Http\Resources\UserResource;
use App\Models\User;

class UserController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        // Obtener todos los usuarios
        $users = User::all();

        // Retornar la colección de usuarios transformados
        return UserResource::collection($users);
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $validated = $request->validate([
            'name'     => 'required|string|max:255',
            'email'    => 'required|email|unique:users,email',
            'password' => 'required|string|min:6',
        ]);

        $user = User::create([
            'name'     => $validated['name'],
            'email'    => $validated['email'],
            'password' => bcrypt($validated['password']),
        ]);
        // Crear token con Sanctum
        $token = $user->createToken('auth_token')->plainTextToken;

        return response()->json([
            'user' => new \App\Http\Resources\UserResource($user),
            'token' => $token,
            //'message' => 'Successfully registered'
        ], 201);

        //return new UserResource($user);
    }

    /**
     * Display the specified resource.
     */
    public function show(User $user)
    {
        return new UserResource($user);
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
{
    // Validar los datos de la solicitud
    $validated = $request->validate([
        'name' => 'required|string|max:255',
        'email' => 'required|email|unique:users,email,' . $id,  // Asegurarse de que el email no esté repetido, excepto para este usuario
        'password' => 'nullable|string|min:8|confirmed',  // La contraseña es opcional, pero si se envía, debe ser confirmada
    ]);

    // Encontrar al usuario por su ID
    $user = User::findOrFail($id);  // Si no se encuentra, Laravel lanza una excepción 404 automáticamente

    // Actualizar los datos del usuario
    $user->update([
        'name' => $validated['name'],
        'email' => $validated['email'],
        'password' => isset($validated['password']) ? bcrypt($validated['password']) : $user->password, // Si no se proporciona una nueva contraseña, se conserva la actual
    ]);

    // Retornar el recurso actualizado
    return new UserResource($user);
}

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($id)
{
    // Buscar al usuario por su ID
    $user = User::findOrFail($id);  // Si no se encuentra el usuario, Laravel lanza una excepción 404 automáticamente

    // Eliminar el usuario
    $user->delete();

    // Retornar una respuesta de éxito
    return response()->json(['message' => 'User deleted successfully'], 200);
}
public function verifyBiometric(Request $request)
{
    // Aquí recibes algo como ['token' => '...'] o el payload que definas
    $token = $request->input('token');
    // Lógica para validar el token/huella, p.ej. buscar un registro en BD
    if ($this->biometricService->validate($token)) {
        return response()->json(['message' => 'Verified'], 200);
    }
    return response()->json(['message' => 'Unauthorized'], 401);
}

}
