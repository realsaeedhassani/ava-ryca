<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Comment extends Model
{
    protected $fillable = [
        'name', 'comment','album_id','rate'
    ];

    public function album()
    {
        return $this->belongsTo(Album::class);
    }
}
