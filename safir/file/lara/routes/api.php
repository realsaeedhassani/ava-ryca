<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();

});
Route::post('comment/{name}', 'CommentController@store');
Route::get('comment/{id}', 'CommentController@index');
Route::get('singers', 'SingerController@index');
Route::get('singers/{id}', 'SingerController@indexAlbum');
Route::get('album/{id}', 'AlbumController@index');
Route::get('album-rate/{id}', 'AlbumController@indexRate');
Route::post('info', 'InfoController@index');
