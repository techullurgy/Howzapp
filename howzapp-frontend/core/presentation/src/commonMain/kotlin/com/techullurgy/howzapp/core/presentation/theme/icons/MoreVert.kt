package com.techullurgy.howzapp.core.presentation.theme.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val more_vert: ImageVector
    get() {
        if (_more_vert != null) {
            return _more_vert!!
        }
        _more_vert =
            ImageVector.Builder(
                name = "more_vert",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(12f, 20.28f)
                        quadToRelative(-0.86f, 0f, -1.48f, -0.61f)
                        reflectiveQuadTo(9.91f, 18.19f)
                        reflectiveQuadToRelative(0.61f, -1.48f)
                        reflectiveQuadTo(12f, 16.09f)
                        quadToRelative(0.87f, 0f, 1.48f, 0.61f)
                        reflectiveQuadToRelative(0.61f, 1.48f)
                        reflectiveQuadToRelative(-0.61f, 1.48f)
                        reflectiveQuadTo(12f, 20.28f)
                        close()
                        moveToRelative(0f, -6.19f)
                        quadToRelative(-0.86f, 0f, -1.48f, -0.61f)
                        reflectiveQuadTo(9.91f, 12f)
                        quadToRelative(0f, -0.87f, 0.61f, -1.48f)
                        reflectiveQuadTo(12f, 9.91f)
                        quadToRelative(0.87f, 0f, 1.48f, 0.61f)
                        reflectiveQuadTo(14.09f, 12f)
                        reflectiveQuadToRelative(-0.61f, 1.48f)
                        reflectiveQuadTo(12f, 14.09f)
                        close()
                        moveTo(12f, 7.91f)
                        quadToRelative(-0.86f, 0f, -1.48f, -0.62f)
                        reflectiveQuadTo(9.91f, 5.81f)
                        reflectiveQuadTo(10.52f, 4.34f)
                        reflectiveQuadTo(12f, 3.72f)
                        quadToRelative(0.87f, 0f, 1.48f, 0.61f)
                        reflectiveQuadToRelative(0.61f, 1.48f)
                        reflectiveQuadTo(13.48f, 7.29f)
                        reflectiveQuadTo(12f, 7.91f)
                        close()
                    }
                }
                .build()
        return _more_vert!!
    }

private var _more_vert: ImageVector? = null