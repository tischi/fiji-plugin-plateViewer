package de.embl.cba.multipositionviewer;

import ij.IJ;
import ij.ImagePlus;
import net.imglib2.cache.img.CachedCellImg;
import net.imglib2.cache.img.ReadOnlyCachedCellImgFactory;
import net.imglib2.cache.img.ReadOnlyCachedCellImgOptions;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.type.numeric.integer.UnsignedShortType;
import net.imglib2.type.numeric.real.FloatType;

import java.io.File;
import java.util.Map;

public class CachedPlateViewImg
{

	private final Map< String, File > cellFileMap;
	private final int[] siteDimensions;
	private final int[] wellDimensions;
	private long[] dimensions;
	private int[] imageDimensions;
	private double[] lutMinMax;
	private final int numIoThreads;

	private int bitDepth;

	public CachedPlateViewImg( Map< String, File > cellFileMap, int[] wellDimensions, int[] siteDimensions, int numIoThreads )
	{
		this.cellFileMap = cellFileMap;
		this.wellDimensions = wellDimensions;
		this.siteDimensions = siteDimensions;
		this.numIoThreads = numIoThreads;

		setImageProperties();
	}

	public int getBitDepth()
	{
		return bitDepth;
	}

	public double[] getLutMinMax()
	{
		return lutMinMax;
	}

	public Map< String, File > getCellFileMap()
	{
		return cellFileMap;
	}

	public int[] getImageDimensions()
	{
		return imageDimensions;
	}

	private void setImageProperties()
	{
		final ImagePlus imagePlus = getFirstImage();

		setImageBitDepth( imagePlus );

		setImageDimensions( imagePlus );

		setImageMinMax( imagePlus );

	}

	private void setImageMinMax( ImagePlus imagePlus )
	{
		lutMinMax = new double[ 2 ];
		lutMinMax[ 0 ] = imagePlus.getProcessor().getMin();
		lutMinMax[ 1 ] = imagePlus.getProcessor().getMax();
	}

	private ImagePlus getFirstImage()
	{
		final String next = cellFileMap.keySet().iterator().next();
		File file = cellFileMap.get( next );
		return IJ.openImage( file.getAbsolutePath() );
	}

	private void setImageBitDepth( ImagePlus imagePlus )
	{
		bitDepth = imagePlus.getBitDepth();
	}

	private void setImageDimensions( ImagePlus imagePlus )
	{
		imageDimensions = new int[ 2 ];
		imageDimensions[ 0 ] = imagePlus.getWidth();
		imageDimensions[ 1 ] = imagePlus.getHeight();

		dimensions = new long[ 2 ];

		for ( int d = 0; d < 2; ++d )
		{
			dimensions[ d ] = imageDimensions[ d ] * wellDimensions[ d ] * siteDimensions[ d ];
		}
	}

	public CachedCellImg getCachedCellImg( )
	{
		final MultiPositionLoader loader = new MultiPositionLoader( imageDimensions, bitDepth, cellFileMap, numIoThreads );

		switch ( bitDepth )
		{
			case 8:

				final CachedCellImg< UnsignedByteType, ? > byteTypeImg = new ReadOnlyCachedCellImgFactory().create(
						dimensions,
						new UnsignedByteType(),
						loader,
						ReadOnlyCachedCellImgOptions.options().cellDimensions( imageDimensions ) );
				return byteTypeImg;

			case 16:

				final CachedCellImg< UnsignedShortType, ? > unsignedShortTypeImg = new ReadOnlyCachedCellImgFactory().create(
						dimensions,
						new UnsignedShortType(),
						loader,
						ReadOnlyCachedCellImgOptions.options().cellDimensions( imageDimensions ) );
				return unsignedShortTypeImg;

			case 32:

				final CachedCellImg< FloatType, ? > floatTypeImg = new ReadOnlyCachedCellImgFactory().create(
						dimensions,
						new UnsignedShortType(),
						loader,
						ReadOnlyCachedCellImgOptions.options().cellDimensions( imageDimensions ) );
				return floatTypeImg;

			default:

				return null;

		}

	}
}