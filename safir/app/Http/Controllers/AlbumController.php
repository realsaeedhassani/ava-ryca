<?php

namespace App\Http\Controllers;

use App\Album;
use App\Comment;
use Illuminate\Http\Request;
use Illuminate\Http\Response;

class AlbumController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @param $id
     * @return Response
     */
    public function index($id)
    {
        return Album::where('singer_id', $id)
            ->orderBy('name')->paginate(2);
    }

    public function indexRate($id)
    {
        return Comment::where('album_id', $id)->avg('rate');
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param \Illuminate\Http\Request $request
     * @return Response
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param \App\Album $album
     * @return Response
     */
    public function show(Album $album)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param \App\Album $album
     * @return Response
     */
    public function edit(Album $album)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param \Illuminate\Http\Request $request
     * @param \App\Album $album
     * @return Response
     */
    public function update(Request $request, Album $album)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param \App\Album $album
     * @return Response
     */
    public function destroy(Album $album)
    {
        //
    }
}
