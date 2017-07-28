package com.kodgames.corgi.core.util.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 无锁环形队列
 * @author cuichao 
 */
public class ConcurrentRingBuffer<T>
{
	private static Logger logger = LoggerFactory.getLogger(ConcurrentRingBuffer.class);
	private int bufferSize = 0;
	private AtomicReferenceArray<T> buffer = null;
	private volatile boolean startFlag = false;
	private volatile boolean endFlag = false;
	private AtomicInteger startPosition = new AtomicInteger(0);
	private AtomicInteger endPosition = new AtomicInteger(0);

	public ConcurrentRingBuffer(int bufferSize)
	{
		this.bufferSize = bufferSize < 16 ? 16 : bufferSize;
		buffer = new AtomicReferenceArray<T>(this.bufferSize);
	}

	public T take() throws EmptyBufferException
	{
		Object stub = ThreadLocalObject.get();

		try
		{
			setThreadContextToThreadLocal();
			isEmpty();
			flipStartFlag();
			takeIndexIncrementForNextTake();
			return returnItem();
		}
		catch(EmptyBufferException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			logger.error("exception arised. ", e);
			return null;
		}
		finally
		{
			if(stub != null)
				ThreadLocalObject.set(stub);
		}
	}

	public void put(T item) throws FullBufferException
	{
		Object stub = ThreadLocalObject.get();

		try
		{
			setThreadContextToThreadLocal();
			isFull();
			flipEndFlag();
			putIndexIncrementForNextPut();
			putItem(item);
		}
		finally
		{
			if(stub != null)
				ThreadLocalObject.set(stub);
		}
	}

	private void flipStartFlag()
	{
		ThreadContext context = getThreadContextFromThreadLocal();
		if (context.getBeginIndex() == 0)
			startFlag = !startFlag;
	}

	private void flipEndFlag()
	{
		ThreadContext context = getThreadContextFromThreadLocal();
		if (context.getEndIndex() == 0)
			endFlag = !endFlag;
	}

	private void putIndexIncrementForNextPut()
	{
		while (true)
		{
			int current = endPosition.get();
			int next = current + 1 == bufferSize ? 0 : current + 1;
			if (endPosition.compareAndSet(current, next))
				break;
		}
	}

	private void takeIndexIncrementForNextTake()
	{
		while (true)
		{
			int current = startPosition.get();
			int next = current + 1 == bufferSize ? 0 : current + 1;
			if (startPosition.compareAndSet(current, next))
				break;
		}
	}

	private T returnItem()
	{
		ThreadContext context = getThreadContextFromThreadLocal();
		return buffer.get(context.getBeginIndex());
	}

	private void putItem(T item)
	{
		ThreadContext context = getThreadContextFromThreadLocal();
		buffer.set(context.getEndIndex(), item);
	}

	private void setThreadContextToThreadLocal()
	{
		ThreadLocalObject.set(new ThreadContext(startPosition.get(), endPosition.get()));
	}
	
	@SuppressWarnings("unchecked")
	private ThreadContext getThreadContextFromThreadLocal() 
	{
		return (ThreadContext)ThreadLocalObject.get();
	}

	private void isEmpty() throws EmptyBufferException
	{
		ThreadContext context = getThreadContextFromThreadLocal();
		int takeIndex = context.getBeginIndex();
		int putIndex = context.getEndIndex();
		if (takeIndex == putIndex && startFlag == endFlag)
			throw new EmptyBufferException("Buffer is empty");
	}

	private void isFull() throws FullBufferException
	{
		ThreadContext context = getThreadContextFromThreadLocal();
		int takeIndex = context.getBeginIndex();
		int putIndex = context.getEndIndex();
		if (takeIndex == putIndex && startFlag != endFlag)
			throw new FullBufferException("Buffer is full");
	}
	
	class ThreadContext
	{
		private int beginIndex = 0;
		private int endIndex = 0;

		ThreadContext(int beginIndex, int endIndex)
		{
			this.beginIndex = beginIndex;
			this.endIndex = endIndex;
		}

		int getBeginIndex()
		{
			return beginIndex;
		}
		void setBeginIndex(int beginIndex)
		{
			this.beginIndex = beginIndex;
		}
		int getEndIndex()
		{
			return endIndex;
		}
		void setEndIndex(int endIndex)
		{
			this.endIndex = endIndex;
		}
	}
	
	public static void main(String args[])
	{
		ConcurrentRingBuffer<Integer> bf = new ConcurrentRingBuffer<Integer>(10);
		class Producer implements Runnable
		{
			private ConcurrentRingBuffer<Integer> bf;
			public Producer(ConcurrentRingBuffer<Integer> bf)
			{
				this.bf = bf;
			}

			@Override
			public void run()
			{
				Integer index = 50;
				while (index > 0)
				{
					try
					{
						bf.put(new Integer(index));
						System.out.println("put: " + index);
						index--;
						Thread.sleep(1000);
					}
					catch (FullBufferException e)
					{
						e.printStackTrace();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		class Consumer implements Runnable
		{
			private ConcurrentRingBuffer<Integer> bf;
			public Consumer(ConcurrentRingBuffer<Integer> bf)
			{
				this.bf = bf;
			}

			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						Integer take = bf.take();
						System.out.println("take: " + take);
						Thread.sleep(3000);
					}
					catch (EmptyBufferException e)
					{
						e.printStackTrace();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}

			}
		}
		Thread producer = new Thread(new Producer(bf));
		Thread consumer = new Thread(new Consumer(bf));
		producer.start();
		consumer.start();
	}
}
