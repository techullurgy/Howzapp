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
val call: ImageVector
    get() {
        if (_call != null) {
            return _call!!
        }
        _call =
            ImageVector.Builder(
                name = "call",
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
                        moveTo(19.96f, 21.2f)
                        quadToRelative(-3.16f, 0f, -6.25f, -1.38f)
                        reflectiveQuadTo(8.09f, 15.92f)
                        reflectiveQuadTo(4.18f, 10.3f)
                        reflectiveQuadTo(2.8f, 4.04f)
                        quadTo(2.8f, 3.51f, 3.16f, 3.15f)
                        reflectiveQuadTo(4.04f, 2.8f)
                        horizontalLineTo(8.09f)
                        quadToRelative(0.49f, 0f, 0.83f, 0.29f)
                        reflectiveQuadTo(9.35f, 3.82f)
                        lineTo(10f, 7.2f)
                        quadTo(10.07f, 7.69f, 9.98f, 8.03f)
                        reflectiveQuadTo(9.61f, 8.62f)
                        lineToRelative(-2.46f, 2.4f)
                        quadToRelative(0.48f, 0.86f, 1.13f, 1.67f)
                        reflectiveQuadToRelative(1.45f, 1.59f)
                        quadToRelative(0.75f, 0.75f, 1.54f, 1.37f)
                        reflectiveQuadToRelative(1.66f, 1.12f)
                        lineTo(15.31f, 14.4f)
                        quadTo(15.6f, 14.12f, 16.03f, 14f)
                        reflectiveQuadToRelative(0.86f, -0.05f)
                        lineToRelative(3.29f, 0.68f)
                        quadToRelative(0.48f, 0.14f, 0.75f, 0.46f)
                        reflectiveQuadToRelative(0.28f, 0.78f)
                        verticalLineToRelative(4.09f)
                        quadToRelative(0f, 0.53f, -0.36f, 0.89f)
                        reflectiveQuadTo(19.96f, 21.2f)
                        close()
                        moveTo(6.06f, 8.9f)
                        lineTo(7.7f, 7.31f)
                        lineTo(7.29f, 5.07f)
                        horizontalLineTo(5.11f)
                        quadTo(5.22f, 6.05f, 5.44f, 7.01f)
                        reflectiveQuadTo(6.06f, 8.9f)
                        close()
                        moveToRelative(8.97f, 8.98f)
                        quadToRelative(0.94f, 0.41f, 1.92f, 0.65f)
                        reflectiveQuadToRelative(1.97f, 0.33f)
                        verticalLineTo(16.7f)
                        lineTo(16.7f, 16.23f)
                        lineToRelative(-1.66f, 1.65f)
                        close()
                        moveTo(6.06f, 8.9f)
                        close()
                        moveToRelative(8.97f, 8.98f)
                        close()
                    }
                }
                .build()
        return _call!!
    }

private var _call: ImageVector? = null