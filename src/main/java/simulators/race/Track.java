package simulators.race;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;

/**
 * Track
 *
 * @author Pedro H. (pedrohcd@hotmail.com)
 *
 */
public class Track {


	final private static Random random = new Random();

	/**
	 * Block
	 * 
	 * @author user
	 *
	 */
	class Block {
		int x;
		int y;
	}
	
	//
	ObstacleRace mRace;

	List<Block> mBlocks = new ArrayList<Block>();

	int mBlockSize;
	int mTrackSize;
	int mTrackSegment;
	float mTrackScrollX = 0;
	float mTrackScrollY = 0;
	float mTotalDistance;
	
	/**
	 * Constructor
	 * 
	 * @param race
	 */
	public Track(ObstacleRace race, int blockSize, int trackSize, int trackSegment) {
		mRace = race;
		mBlockSize = blockSize;
		mTrackSize = trackSize;
		mTrackSegment = trackSegment;
		createTrack();
		mTotalDistance = calcTotalDistance();
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		mTrackScrollX = 0;
		mTrackScrollY = 0;
	}
	
	/**
	 * Scroll
	 * 
	 * @param scroll
	 */
	public void setScroll(float scrollX, float scrollY) {
		mTrackScrollX = scrollX;
		mTrackScrollY = scrollY;
	}
	
	/**
	 * Create Track
	 */
	public void createTrack() {
		mBlocks.clear();
		mRace.getApplet().noiseSeed(random.nextInt());
		int axis = -1;

		// First Block
		int lastY = mRace.getWindowHeight() / (mBlockSize * 2);
		Block firstBlock = new Block();
		firstBlock.x = 0;
		firstBlock.y = lastY*mBlockSize;
		mBlocks.add(firstBlock);

		// Others blocks
		for(int x=0; x<mTrackSize; x+=1) {
			int y = (int)(mRace.getApplet().noise(x/mTrackSegment) * 10) + axis;
			Block block = new Block();
			block.x = x*mBlockSize;
			block.y = y*mBlockSize;
			mBlocks.add(block);
			// Conect Points
			if(y > lastY) {
				while(y != lastY) {
					Block conect = new Block();
					conect.x = x*mBlockSize;
					conect.y = lastY*mBlockSize;
					mBlocks.add(conect);
					lastY++;
				}
			} else if(y < lastY) {
				while(y != lastY) {
					Block conect = new Block();
					conect.x = x*mBlockSize;
					conect.y = lastY*mBlockSize;
					mBlocks.add(conect);
					lastY--;
				}
			}
			lastY = y;
		}
	}
	
	/**
	 * Get Start Position
	 * @return
	 */
	public float[] getStartPosition() {
		int x = mBlocks.get(0).x + mBlockSize / 2;
		int y = mBlocks.get(0).y + mBlockSize / 2;
		return new float[] {x, y};
	}
	
	/**
	 * Get Start Direction
	 * @return
	 */
	public float getStartDirection() {
		return 0;
	}
	
	/**
	 * Is Over Track
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isOverTrack(float x, float y) {
		for(Block block : mBlocks) {
			if(x >= block.x && x < (block.x + mBlockSize) && y >= block.y && y < (block.y + mBlockSize))
				return true;
		}
		return false;
	}
	
	/**
	 * Get Block Id
	 * @param x
	 * @param y
	 * @return
	 */
	public int getBlockId(float x, float y) {
		for(int i=0; i<mBlocks.size(); i++) {
			Block block = mBlocks.get(i);
			if(x >= block.x && x < (block.x + mBlockSize) && y >= block.y && y < (block.y + mBlockSize))
				return i;
		}
		return -1;
	}
	
	
	/**
	 * Clamp Position
	 * @param x
	 * @param y
	 * @return
	 */
	public float[] clampPosition(float lx, float ly, float x, float y) {
		// Not clamp
		if(isOverTrack(x, y))
			return new float[] {x, y};
		// Get over block
		Block over = null;
		for(Block block : mBlocks) {
			if(lx >= block.x && lx < (block.x + mBlockSize) && ly >= block.y && ly < (block.y + mBlockSize)) {
				over = block;
				break;
			}
		}
		// Correct if over not found
		if(over == null) {
			float dMin = Float.MAX_VALUE;
			for(Block block : mBlocks) {
				int bx = block.x + mBlockSize / 2;
				int by = block.y + mBlockSize / 2;
				float d = (float)Math.hypot(bx - lx, by - ly);
				if(d < dMin) {
					dMin = d;
					over = block;
				}
			}
		}
		// Clamp position
		float nx = Math.min(over.x + mBlockSize, Math.max(over.x, x));
		float ny = Math.min(over.y + mBlockSize, Math.max(over.y, y));
		return new float[] {nx, ny};
	}
	
	/**
	 * Over Block
	 * 
	 * @param block
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean overBlock(Block block, float x, float y) {
		if((x+2 >= block.x || x-2 >= block.x)  && (x+2 < (block.x + mBlockSize) || x-2 < (block.x + mBlockSize)) &&
		   (y+2 >= block.y || y-2 >= block.y) && (y+2 < (block.y + mBlockSize) || y-2 < (block.y + mBlockSize))) {
			return true;
		}
		return false;
	}
	
	/**
	 * Distance to Goal
	 * @param x
	 * @param y
	 * @return
	 */
	public float getDistanceToGoal(float x, float y) {
		float distance = 0;
		boolean found = false;
		for(int i=0; i<mBlocks.size(); i++) {
			Block block = mBlocks.get(i);
			if(found) {
				if(i == mBlocks.size() - 1) {
					float bx = block.x + mBlockSize/2;
					float by = block.y + mBlockSize/2;
					float nbx = block.x + mBlockSize;
					float nby = block.y + mBlockSize/2;
					distance += (float)Math.hypot(nbx - bx, nby - by);
				} else  {
					Block nextBlock = mBlocks.get(i + 1);
					float bx = block.x + mBlockSize/2;
					float by = block.y + mBlockSize/2;
					float nbx = nextBlock.x + mBlockSize/2;
					float nby = nextBlock.y + mBlockSize/2;
					distance += (float)Math.hypot(nbx - bx, nby - by);
				}
			} else {
				if(overBlock(block, x, y)) {
					if(i == mBlocks.size() - 1) {
						float nbx = block.x + mBlockSize;
						float nby = block.y + mBlockSize/2;
						distance += (float)Math.hypot(nbx - x, nby - y);
					} else  {
						Block nextBlock = mBlocks.get(i + 1);
						float nbx = nextBlock.x + mBlockSize/2;
						float nby = nextBlock.y + mBlockSize/2;
						distance += (float)Math.hypot(nbx - x, nby - y);
						found = true;
					}
				}
			}
		}
		return distance;
	}
	
	/**
	 * Get Total Distance
	 * @return
	 */
	public float getTotalDistance() {
		return mTotalDistance;
	}
	
	/**
	 * Total Distance
	 * @return
	 */
	public float calcTotalDistance() {
		float distance = 0;
		for(int i=0; i<mBlocks.size(); i++) {
			Block block = mBlocks.get(i);
			if(i == mBlocks.size() - 1) {
				float bx = block.x + mBlockSize/2;
				float by = block.y + mBlockSize/2;
				float nbx = block.x + mBlockSize;
				float nby = block.y + mBlockSize/2;
				distance += (float)Math.hypot(nbx - bx, nby - by);
			} else  {
				Block nextBlock = mBlocks.get(i + 1);
				float bx = block.x + mBlockSize/2;
				float by = block.y + mBlockSize/2;
				float nbx = nextBlock.x + mBlockSize/2;
				float nby = nextBlock.y + mBlockSize/2;
				distance += (float)Math.hypot(nbx - bx, nby - by);
			}
		}
		return distance;
	}
	
	/**
	 * Get Ray Distance
	 * @param x
	 * @param y
	 * @param dx
	 * @param dy
	 * @return
	 */
	public float getRayDistance(float x, float y, float dx, float dy) {
		float nx = x;
		float ny = y;
		while(isOverTrack(nx, ny)) {
			nx += dx;
			ny += dy;
		}
		return (float)Math.hypot(nx-x, ny-y);
	}
	
	/**
	 * Draw
	 */
	public void draw() {
		mRace.getApplet().fill(255, 0, 0);
		for(Block block : mBlocks) {
			float x = block.x-mTrackScrollX;
			float y = block.y-mTrackScrollY;
			if((x + mBlockSize) < 0 || x >= 640)
				continue;
			mRace.getApplet().rect(x, y, mBlockSize+1, mBlockSize+1);
		}
	}
}
