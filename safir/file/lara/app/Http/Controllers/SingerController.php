<?php

namespace App\Http\Controllers;

use App\Album;
use App\Singer;
use Illuminate\Http\Request;

class SingerController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        return Singer::orderBy('name')->paginate(2);
    }
    public function indexAlbum($id)
    {
        return Album::where('singer_id', $id)->count();
    }
    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param \Illuminate\Http\Request $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param \App\Singer $singer
     * @return \Illuminate\Http\Response
     */
    public function show(Singer $singer)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param \App\Singer $singer
     * @return \Illuminate\Http\Response
     */
    public function edit(Singer $singer)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param \Illuminate\Http\Request $request
     * @param \App\Singer $singer
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Singer $singer)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param \App\Singer $singer
     * @return \Illuminate\Http\Response
     */
    public function destroy(Singer $singer)
    {
        //
    }
}
