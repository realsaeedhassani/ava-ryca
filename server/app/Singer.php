<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Singer extends Model
{
    protected $fillable = [
        'name','url'
    ];
    public function album()
    {
        return $this->hasMany(Album::class);
    }
}
