<?php

namespace App\Http\Resources;

use App\Album;
use Illuminate\Http\Resources\Json\JsonResource;

class Singer extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        return [
            'id' => $this->id,
            'name' => $this->name,
            'count' => Album::where('singer_id', $this->id)->count(),
            'created_at' => $this->created_at
        ];
    }
}
