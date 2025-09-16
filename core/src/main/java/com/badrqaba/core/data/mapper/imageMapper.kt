package com.badrqaba.core.data.mapper

import com.badrqaba.core.data.local.entity.ImageEntity
import com.badrqaba.core.data.remote.dto.ImageDTO
import com.badrqaba.core.domain.model.Image

fun ImageEntity.toImage(): Image {
    return Image(
        uuid = uuid,
        filename = filename,
        preview = preview,
        original = original
    )
}

fun Image.toImageEntity(modelId: Long): ImageEntity {
    return ImageEntity(
        uuid = uuid,
        modelId = modelId,
        filename = filename,
        preview = preview,
        original = original
    )
}

fun ImageDTO.toImageEntity(modelId: Long): ImageEntity {
    return ImageEntity(
        uuid = uuid,
        modelId = modelId,
        filename = filename,
        preview = preview,
        original = original
    )
}

fun ImageDTO.toImage(): Image {
    return Image(
        uuid = uuid,
        filename = filename,
        preview = preview,
        original = original
    )
}