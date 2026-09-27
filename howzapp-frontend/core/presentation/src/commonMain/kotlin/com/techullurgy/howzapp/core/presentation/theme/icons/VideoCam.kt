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
val videocam: ImageVector
    get() {
        if (_videocam != null) {
            return _videocam!!
        }
        _videocam =
            ImageVector.Builder(
                name = "videocam",
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
                        moveTo(4.07f, 20.2f)
                        quadToRelative(-0.94f, 0f, -1.61f, -0.67f)
                        reflectiveQuadTo(1.8f, 17.93f)
                        verticalLineTo(6.07f)
                        quadTo(1.8f, 5.13f, 2.46f, 4.46f)
                        reflectiveQuadTo(4.07f, 3.8f)
                        horizontalLineTo(15.93f)
                        quadToRelative(0.94f, 0f, 1.61f, 0.67f)
                        reflectiveQuadTo(18.2f, 6.07f)
                        verticalLineTo(10.5f)
                        lineToRelative(4f, -4f)
                        verticalLineToRelative(11f)
                        lineToRelative(-4f, -4f)
                        verticalLineToRelative(4.43f)
                        quadToRelative(0f, 0.94f, -0.67f, 1.61f)
                        reflectiveQuadTo(15.93f, 20.2f)
                        horizontalLineTo(4.07f)
                        close()
                        moveToRelative(0f, -2.28f)
                        horizontalLineTo(15.93f)
                        verticalLineTo(6.07f)
                        horizontalLineTo(4.07f)
                        verticalLineTo(17.93f)
                        close()
                        moveToRelative(0f, 0f)
                        verticalLineTo(6.07f)
                        verticalLineTo(17.93f)
                        close()
                    }
                }
                .build()
        return _videocam!!
    }

private var _videocam: ImageVector? = null