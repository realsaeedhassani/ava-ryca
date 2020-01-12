<?php

namespace App\Http\Controllers;

use App\Album;
use App\Comment;
use Illuminate\Http\Request;

class CommentController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index($id)
    {
        return Comment::where('album_id', $id)
            ->orderBy('created_at')->paginate(2);
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
     * @param Request $params
     * @return \Illuminate\Http\Response
     */
    public function store(Request $params)
    {
        try {
            $comment = new Comment([
                'name' => $params['name'],
                'comment' => $params['comment'],
                'album_id' => $params['album_id'],
                'rate' => $params['rate'],
                'info' => $params['info']
            ]);
            $comment->save();
            return response()->json(['response' => ['msg' => 200]], 200);
        } catch (\Exception $exp) {
            return response()->json(['msg' => $exp], 500);
        }
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Comment  $comment
     * @return \Illuminate\Http\Response
     */
    public function show(Comment $comment)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Comment  $comment
     * @return \Illuminate\Http\Response
     */
    public function edit(Comment $comment)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Comment  $comment
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Comment $comment)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Comment  $comment
     * @return \Illuminate\Http\Response
     */
    public function destroy(Comment $comment)
    {
        //
    }
}
