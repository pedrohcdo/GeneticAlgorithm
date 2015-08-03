package ObstacleRace;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PShape;

public class Track {

	int BLOCK_SIZE = 60;
	int TRACK_SIZE = 50;
	int TRACK_SEGMENT = 4;
	
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
	PApplet mApplet;
	List<Block> mBlocks = new ArrayList<Block>();
	
	float mTrackScroll = 0;
	float mTotalDistance;
	
	/**
	 * Constructor
	 * 
	 * @param applet
	 */
	public Track(PApplet applet) {
		mApplet = applet;
		createTrack();
		mTotalDistance = calcTotalDistance();
	}
	
	/**
	 * Reset
	 */
	public void reset() {
		mTrackScroll = 0;
	}
	
	/**
	 * Scroll
	 * 
	 * @param scroll
	 */
	public void setScroll(float scroll) {
		mTrackScroll = scroll;
	}
	
	/**
	 * Create Track
	 */
	public void createTrack() {
		mBlocks.clear();
		mApplet.noiseSeed(300);
		int axis = -1;
		int lastY = (int)(mApplet.noise(0) * 10) + axis;
		for(int x=0; x<TRACK_SIZE; x+=1) {
			int y = (int)(mApplet.noise(x/TRACK_SEGMENT) * 10) + axis;
			Block block = new Block();
			block.x = x*BLOCK_SIZE;
			block.y = y*BLOCK_SIZE;
			mBlocks.add(block);
			// Conect Points
			if(y > lastY) {
				while(y != lastY) {
					Block conect = new Block();
					conect.x = x*BLOCK_SIZE;
					conect.y = lastY*BLOCK_SIZE;
					mBlocks.add(conect);
					lastY++;
				}
			} else if(y < lastY) {
				while(y != lastY) {
					Block conect = new Block();
					conect.x = x*BLOCK_SIZE;
					conect.y = lastY*BLOCK_SIZE;
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
		int x = mBlocks.get(0).x + BLOCK_SIZE / 2;
		int y = mBlocks.get(0).y + BLOCK_SIZE / 2;
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
			if(x >= block.x && x < (block.x + BLOCK_SIZE) && y >= block.y && y < (block.y + BLOCK_SIZE))
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
			if(x >= block.x && x < (block.x + BLOCK_SIZE) && y >= block.y && y < (block.y + BLOCK_SIZE))
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
			if(lx >= block.x && lx < (block.x + BLOCK_SIZE) && ly >= block.y && ly < (block.y + BLOCK_SIZE)) {
				over = block;
				break;
			}
		}
		// Correct if over not found
		if(over == null) {
			float dMin = Float.MAX_VALUE;
			for(Block block : mBlocks) {
				int bx = block.x + BLOCK_SIZE / 2;
				int by = block.y + BLOCK_SIZE / 2;
				float d = (float)Math.hypot(bx - lx, by - ly);
				if(d < dMin) {
					dMin = d;
					over = block;
				}
			}
		}
		// Clamp position
		float nx = Math.min(over.x + BLOCK_SIZE, Math.max(over.x, x));
		float ny = Math.min(over.y + BLOCK_SIZE, Math.max(over.y, y));
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
		if((x+2 >= block.x || x-2 >= block.x)  && (x+2 < (block.x + BLOCK_SIZE) || x-2 < (block.x + BLOCK_SIZE)) &&
		   (y+2 >= block.y || y-2 >= block.y) && (y+2 < (block.y + BLOCK_SIZE) || y-2 < (block.y + BLOCK_SIZE))) {
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
					float bx = block.x + BLOCK_SIZE/2;
					float by = block.y + BLOCK_SIZE/2;
					float nbx = block.x + BLOCK_SIZE;
					float nby = block.y + BLOCK_SIZE/2;
					distance += (float)Math.hypot(nbx - bx, nby - by);
				} else  {
					Block nextBlock = mBlocks.get(i + 1);
					float bx = block.x + BLOCK_SIZE/2;
					float by = block.y + BLOCK_SIZE/2;
					float nbx = nextBlock.x + BLOCK_SIZE/2;
					float nby = nextBlock.y + BLOCK_SIZE/2;
					distance += (float)Math.hypot(nbx - bx, nby - by);
				}
			} else {
				if(overBlock(block, x, y)) {
					if(i == mBlocks.size() - 1) {
						float nbx = block.x + BLOCK_SIZE;
						float nby = block.y + BLOCK_SIZE/2;
						distance += (float)Math.hypot(nbx - x, nby - y);
					} else  {
						Block nextBlock = mBlocks.get(i + 1);
						float nbx = nextBlock.x + BLOCK_SIZE/2;
						float nby = nextBlock.y + BLOCK_SIZE/2;
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
				float bx = block.x + BLOCK_SIZE/2;
				float by = block.y + BLOCK_SIZE/2;
				float nbx = block.x + BLOCK_SIZE;
				float nby = block.y + BLOCK_SIZE/2;
				distance += (float)Math.hypot(nbx - bx, nby - by);
			} else  {
				Block nextBlock = mBlocks.get(i + 1);
				float bx = block.x + BLOCK_SIZE/2;
				float by = block.y + BLOCK_SIZE/2;
				float nbx = nextBlock.x + BLOCK_SIZE/2;
				float nby = nextBlock.y + BLOCK_SIZE/2;
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
		mApplet.fill(255, 0, 0);
		for(Block block : mBlocks) {
			float x = block.x-mTrackScroll;
			int y = block.y;
			if((x + BLOCK_SIZE) < 0 || x >= 640)
				continue;
			mApplet.rect(x, y, BLOCK_SIZE+1, BLOCK_SIZE+1);
		}
	}
}
