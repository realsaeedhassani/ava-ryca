<?php

namespace App\Http\Resources;

use App\Comment;
use Illuminate\Http\Resources\Json\JsonResource;

class AlbumResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param \Illuminate\Http\Request $request
     * @return array
     */
    public function toArray($request)
    {
        return [
            'id' => $this->id,
            'name' => $this->name,
            'sid' => $this->singer_id,
            'cc' => Comment::where('album_id', $this->id)->count(),
            'rate' => Comment::where('album_id', $this->id)->avg('rate')
        ];
    }
}
