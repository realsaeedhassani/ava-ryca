<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Album extends Model
{
    protected $fillable = [
        'name', 'url'
    ];
    public function singer()
    {
        return $this->belongsTo(Singer::class);
    }
    public function comment()
    {
        return $this->hasMany(Comment::class);
    }
}
