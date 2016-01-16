/*
 * Copyright 2015 peter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package onl.area51.mapgen.util;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author peter
 */
public enum ImageType
{

    /**
     * Represents an image with 8-bit RGB color components packed into
     * integer pixels. The image has a {@link DirectColorModel} without
     * alpha.
     * When data with non-opaque alpha is stored
     * in an image of this type,
     * the color data must be adjusted to a non-premultiplied form
     * and the alpha discarded,
     * as described in the
     * {@link java.awt.AlphaComposite} documentation.
     */
    INT_RGB( BufferedImage.TYPE_INT_RGB ),

    /**
     * Represents an image with 8-bit RGBA color components packed into
     * integer pixels. The image has a <code>DirectColorModel</code>
     * with alpha. The color data in this image is considered not to be
     * premultiplied with alpha. When this type is used as the
     * <code>imageType</code> argument to a <code>BufferedImage</code>
     * constructor, the created image is consistent with images
     * created in the JDK1.1 and earlier releases.
     */
    INT_ARGB( BufferedImage.TYPE_INT_ARGB ),

    /**
     * Represents an image with 8-bit RGBA color components packed into
     * integer pixels. The image has a <code>DirectColorModel</code>
     * with alpha. The color data in this image is considered to be
     * premultiplied with alpha.
     */
    INT_ARGB_PRE( BufferedImage.TYPE_INT_ARGB_PRE ),

    /**
     * Represents an image with 8-bit RGB color components, corresponding
     * to a Windows- or Solaris- style BGR color model, with the colors
     * Blue, Green, and Red packed into integer pixels. There is no alpha.
     * The image has a {@link DirectColorModel}.
     * When data with non-opaque alpha is stored
     * in an image of this type,
     * the color data must be adjusted to a non-premultiplied form
     * and the alpha discarded,
     * as described in the
     * {@link java.awt.AlphaComposite} documentation.
     */
    INT_BGR( BufferedImage.TYPE_INT_BGR ),

    /**
     * Represents an image with 8-bit RGB color components, corresponding
     * to a Windows-style BGR color model) with the colors Blue, Green,
     * and Red stored in 3 bytes. There is no alpha. The image has a
     * <code>ComponentColorModel</code>.
     * When data with non-opaque alpha is stored
     * in an image of this type,
     * the color data must be adjusted to a non-premultiplied form
     * and the alpha discarded,
     * as described in the
     * {@link java.awt.AlphaComposite} documentation.
     */
    _3BYTE_BGR( BufferedImage.TYPE_3BYTE_BGR ),

    /**
     * Represents an image with 8-bit RGBA color components with the colors
     * Blue, Green, and Red stored in 3 bytes and 1 byte of alpha. The
     * image has a <code>ComponentColorModel</code> with alpha. The
     * color data in this image is considered not to be premultiplied with
     * alpha. The byte data is interleaved in a single
     * byte array in the order A, B, G, R
     * from lower to higher byte addresses within each pixel.
     */
    _4BYTE_ABGR( BufferedImage.TYPE_4BYTE_ABGR ),

    /**
     * Represents an image with 8-bit RGBA color components with the colors
     * Blue, Green, and Red stored in 3 bytes and 1 byte of alpha. The
     * image has a <code>ComponentColorModel</code> with alpha. The color
     * data in this image is considered to be premultiplied with alpha.
     * The byte data is interleaved in a single byte array in the order
     * A, B, G, R from lower to higher byte addresses within each pixel.
     */
    _4BYTE_ABGR_PRE( BufferedImage.TYPE_4BYTE_ABGR_PRE ),

    /**
     * Represents an image with 5-6-5 RGB color components (5-bits red,
     * 6-bits green, 5-bits blue) with no alpha. This image has
     * a <code>DirectColorModel</code>.
     * When data with non-opaque alpha is stored
     * in an image of this type,
     * the color data must be adjusted to a non-premultiplied form
     * and the alpha discarded,
     * as described in the
     * {@link java.awt.AlphaComposite} documentation.
     */
    USHORT_565_RGB( BufferedImage.TYPE_USHORT_565_RGB ),

    /**
     * Represents an image with 5-5-5 RGB color components (5-bits red,
     * 5-bits green, 5-bits blue) with no alpha. This image has
     * a <code>DirectColorModel</code>.
     * When data with non-opaque alpha is stored
     * in an image of this type,
     * the color data must be adjusted to a non-premultiplied form
     * and the alpha discarded,
     * as described in the
     * {@link java.awt.AlphaComposite} documentation.
     */
    USHORT_555_RGB( BufferedImage.TYPE_USHORT_555_RGB ),

    /**
     * Represents a unsigned byte grayscale image, non-indexed. This
     * image has a <code>ComponentColorModel</code> with a CS_GRAY
     * {@link ColorSpace}.
     * When data with non-opaque alpha is stored
     * in an image of this type,
     * the color data must be adjusted to a non-premultiplied form
     * and the alpha discarded,
     * as described in the
     * {@link java.awt.AlphaComposite} documentation.
     */
    BYTE_GRAY( BufferedImage.TYPE_BYTE_GRAY ),

    /**
     * Represents an unsigned short grayscale image, non-indexed). This
     * image has a <code>ComponentColorModel</code> with a CS_GRAY
     * <code>ColorSpace</code>.
     * When data with non-opaque alpha is stored
     * in an image of this type,
     * the color data must be adjusted to a non-premultiplied form
     * and the alpha discarded,
     * as described in the
     * {@link java.awt.AlphaComposite} documentation.
     */
    USHORT_GRAY( BufferedImage.TYPE_USHORT_GRAY ),

    /**
     * Represents an opaque byte-packed 1, 2, or 4 bit image. The
     * image has an {@link IndexColorModel} without alpha. When this
     * type is used as the <code>imageType</code> argument to the
     * <code>BufferedImage</code> constructor that takes an
     * <code>imageType</code> argument but no <code>ColorModel</code>
     * argument, a 1-bit image is created with an
     * <code>IndexColorModel</code> with two colors in the default
     * sRGB <code>ColorSpace</code>: {0,&nbsp;0,&nbsp;0} and
     * {255,&nbsp;255,&nbsp;255}.
     *
     * <p>
     * Images with 2 or 4 bits per pixel may be constructed via
     * the <code>BufferedImage</code> constructor that takes a
     * <code>ColorModel</code> argument by supplying a
     * <code>ColorModel</code> with an appropriate map size.
     *
     * <p>
     * Images with 8 bits per pixel should use the image types
     * <code>TYPE_BYTE_INDEXED</code> or <code>TYPE_BYTE_GRAY</code>
     * depending on their <code>ColorModel</code>.
     * <p>
     * <p>
     * When color data is stored in an image of this type,
     * the closest color in the colormap is determined
     * by the <code>IndexColorModel</code> and the resulting index is stored.
     * Approximation and loss of alpha or color components
     * can result, depending on the colors in the
     * <code>IndexColorModel</code> colormap.
     */
    BYTE_BINARY( BufferedImage.TYPE_BYTE_BINARY ),

    /**
     * Represents an indexed byte image. When this type is used as the
     * <code>imageType</code> argument to the <code>BufferedImage</code>
     * constructor that takes an <code>imageType</code> argument
     * but no <code>ColorModel</code> argument, an
     * <code>IndexColorModel</code> is created with
     * a 256-color 6/6/6 color cube palette with the rest of the colors
     * from 216-255 populated by grayscale values in the
     * default sRGB ColorSpace.
     *
     * <p>
     * When color data is stored in an image of this type,
     * the closest color in the colormap is determined
     * by the <code>IndexColorModel</code> and the resulting index is stored.
     * Approximation and loss of alpha or color components
     * can result, depending on the colors in the
     * <code>IndexColorModel</code> colormap.
     */
    BYTE_INDEXED( BufferedImage.TYPE_BYTE_INDEXED );
    private final int type;
    private static final Map<String, ImageType> TYPES = new ConcurrentHashMap<>();

    static {
        for( ImageType t: values() ) {
            String n = t.name();
            if( n.startsWith( "_" ) ) {
                n = n.substring( 1 );
            }
            TYPES.put( n, t );
        }
    }

    public static ImageType lookup( String n )
    {
        return TYPES.getOrDefault( n == null ? "" : n.toUpperCase().trim(), INT_RGB );
    }

    private ImageType( int type )
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }

    public BufferedImage create( int w, int h )
    {
        return new BufferedImage( h, h, type );
    }
}
