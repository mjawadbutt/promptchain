package com.promptwise.promptchain.common.util;

import org.redisson.api.*;
import org.redisson.api.options.ClientSideCachingOptions;
import org.redisson.api.options.CommonOptions;
import org.redisson.api.options.JsonBucketOptions;
import org.redisson.api.options.KeysOptions;
import org.redisson.api.options.LiveObjectOptions;
import org.redisson.api.options.OptionalOptions;
import org.redisson.api.options.PatternTopicOptions;
import org.redisson.api.options.PlainOptions;
import org.redisson.api.redisnode.BaseRedisNodes;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonCodec;
import org.redisson.config.Config;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class DummyRedissonClient implements RedissonClient {
  @Override
  public <V, L> RTimeSeries<V, L> getTimeSeries(final String name) {
    return null;
  }

  @Override
  public <V, L> RTimeSeries<V, L> getTimeSeries(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V, L> RTimeSeries<V, L> getTimeSeries(final PlainOptions options) {
    return null;
  }

  @Override
  public <K, V> RStream<K, V> getStream(final String name) {
    return null;
  }

  @Override
  public <K, V> RStream<K, V> getStream(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <K, V> RStream<K, V> getStream(final PlainOptions options) {
    return null;
  }

  @Override
  public RSearch getSearch() {
    return null;
  }

  @Override
  public RSearch getSearch(final Codec codec) {
    return null;
  }

  @Override
  public RSearch getSearch(final OptionalOptions options) {
    return null;
  }

  @Override
  public RRateLimiter getRateLimiter(final String name) {
    return null;
  }

  @Override
  public RRateLimiter getRateLimiter(final CommonOptions options) {
    return null;
  }

  @Override
  public RBinaryStream getBinaryStream(final String name) {
    return null;
  }

  @Override
  public RBinaryStream getBinaryStream(final CommonOptions options) {
    return null;
  }

  @Override
  public <V> RGeo<V> getGeo(final String name) {
    return null;
  }

  @Override
  public <V> RGeo<V> getGeo(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RGeo<V> getGeo(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RSetCache<V> getSetCache(final String name) {
    return null;
  }

  @Override
  public <V> RSetCache<V> getSetCache(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RSetCache<V> getSetCache(final PlainOptions options) {
    return null;
  }

  @Override
  public <K, V> RMapCache<K, V> getMapCache(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <K, V> RMapCache<K, V> getMapCache(final String name, final Codec codec, final MapCacheOptions<K, V> options) {
    return null;
  }

  @Override
  public <K, V> RMapCache<K, V> getMapCache(final org.redisson.api.options.MapCacheOptions<K, V> options) {
    return null;
  }

  @Override
  public <K, V> RMapCache<K, V> getMapCache(final String name) {
    return null;
  }

  @Override
  public <K, V> RMapCache<K, V> getMapCache(final String name, final MapCacheOptions<K, V> options) {
    return null;
  }

  @Override
  public <V> RBucket<V> getBucket(final String name) {
    return null;
  }

  @Override
  public <V> RBucket<V> getBucket(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RBucket<V> getBucket(final PlainOptions options) {
    return null;
  }

  @Override
  public RBuckets getBuckets() {
    return null;
  }

  @Override
  public RBuckets getBuckets(final Codec codec) {
    return null;
  }

  @Override
  public RBuckets getBuckets(final OptionalOptions options) {
    return null;
  }

  @Override
  public <V> RJsonBucket<V> getJsonBucket(final String name, final JsonCodec codec) {
    return null;
  }

  @Override
  public <V> RJsonBucket<V> getJsonBucket(final JsonBucketOptions<V> options) {
    return null;
  }

  @Override
  public RJsonBuckets getJsonBuckets(final JsonCodec codec) {
    return null;
  }

  @Override
  public <V> RHyperLogLog<V> getHyperLogLog(final String name) {
    return null;
  }

  @Override
  public <V> RHyperLogLog<V> getHyperLogLog(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RHyperLogLog<V> getHyperLogLog(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RList<V> getList(final String name) {
    return null;
  }

  @Override
  public <V> RList<V> getList(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RList<V> getList(final PlainOptions options) {
    return null;
  }

  @Override
  public <K, V> RListMultimap<K, V> getListMultimap(final String name) {
    return null;
  }

  @Override
  public <K, V> RListMultimap<K, V> getListMultimap(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <K, V> RListMultimap<K, V> getListMultimap(final PlainOptions options) {
    return null;
  }

  @Override
  public <K, V> RListMultimapCache<K, V> getListMultimapCache(final String name) {
    return null;
  }

  @Override
  public <K, V> RListMultimapCache<K, V> getListMultimapCache(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <K, V> RListMultimapCache<K, V> getListMultimapCache(final PlainOptions options) {
    return null;
  }

  @Override
  public <K, V> RLocalCachedMapCache<K, V> getLocalCachedMapCache(final String name, final LocalCachedMapCacheOptions<K, V> options) {
    return null;
  }

  @Override
  public <K, V> RLocalCachedMapCache<K, V> getLocalCachedMapCache(final String name, final Codec codec, final LocalCachedMapCacheOptions<K, V> options) {
    return null;
  }

  @Override
  public <K, V> RListMultimapCacheNative<K, V> getListMultimapCacheNative(final String name) {
    return null;
  }

  @Override
  public <K, V> RListMultimapCacheNative<K, V> getListMultimapCacheNative(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <K, V> RListMultimapCacheNative<K, V> getListMultimapCacheNative(final PlainOptions options) {
    return null;
  }

  @Override
  public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(final String name, final LocalCachedMapOptions<K, V> options) {
    return null;
  }

  @Override
  public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(final String name, final Codec codec, final LocalCachedMapOptions<K, V> options) {
    return null;
  }

  @Override
  public <K, V> RLocalCachedMap<K, V> getLocalCachedMap(final org.redisson.api.options.LocalCachedMapOptions<K, V> options) {
    return null;
  }

  @Override
  public <K, V> RMap<K, V> getMap(final String name) {
    return null;
  }

  @Override
  public <K, V> RMap<K, V> getMap(final String name, final MapOptions<K, V> options) {
    return null;
  }

  @Override
  public <K, V> RMap<K, V> getMap(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <K, V> RMap<K, V> getMap(final String name, final Codec codec, final MapOptions<K, V> options) {
    return null;
  }

  @Override
  public <K, V> RMap<K, V> getMap(final org.redisson.api.options.MapOptions<K, V> options) {
    return null;
  }

  @Override
  public <K, V> RMapCacheNative<K, V> getMapCacheNative(final String name) {
    return null;
  }

  @Override
  public <K, V> RMapCacheNative<K, V> getMapCacheNative(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <K, V> RMapCacheNative<K, V> getMapCacheNative(final org.redisson.api.options.MapOptions<K, V> options) {
    return null;
  }

  @Override
  public <K, V> RSetMultimap<K, V> getSetMultimap(final String name) {
    return null;
  }

  @Override
  public <K, V> RSetMultimap<K, V> getSetMultimap(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <K, V> RSetMultimap<K, V> getSetMultimap(final PlainOptions options) {
    return null;
  }

  @Override
  public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(final String name) {
    return null;
  }

  @Override
  public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <K, V> RSetMultimapCache<K, V> getSetMultimapCache(final PlainOptions options) {
    return null;
  }

  @Override
  public <K, V> RSetMultimapCacheNative<K, V> getSetMultimapCacheNative(final String name) {
    return null;
  }

  @Override
  public <K, V> RSetMultimapCacheNative<K, V> getSetMultimapCacheNative(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <K, V> RSetMultimapCacheNative<K, V> getSetMultimapCacheNative(final PlainOptions options) {
    return null;
  }

  @Override
  public RSemaphore getSemaphore(final String name) {
    return null;
  }

  @Override
  public RSemaphore getSemaphore(final CommonOptions options) {
    return null;
  }

  @Override
  public RPermitExpirableSemaphore getPermitExpirableSemaphore(final String name) {
    return null;
  }

  @Override
  public RPermitExpirableSemaphore getPermitExpirableSemaphore(final CommonOptions options) {
    return null;
  }

  @Override
  public RLock getLock(final String name) {
    return null;
  }

  @Override
  public RLock getLock(final CommonOptions options) {
    return null;
  }

  @Override
  public RLock getSpinLock(final String name) {
    return null;
  }

  @Override
  public RLock getSpinLock(final String name, final LockOptions.BackOff backOff) {
    return null;
  }

  @Override
  public RFencedLock getFencedLock(final String name) {
    return null;
  }

  @Override
  public RFencedLock getFencedLock(final CommonOptions options) {
    return null;
  }

  @Override
  public RLock getMultiLock(final RLock... locks) {
    return null;
  }

  @Override
  public RLock getMultiLock(final String group, final Collection<Object> values) {
    return null;
  }

  @Override
  public RLock getRedLock(final RLock... locks) {
    return null;
  }

  @Override
  public RLock getFairLock(final String name) {
    return null;
  }

  @Override
  public RLock getFairLock(final CommonOptions options) {
    return null;
  }

  @Override
  public RReadWriteLock getReadWriteLock(final String name) {
    return null;
  }

  @Override
  public RReadWriteLock getReadWriteLock(final CommonOptions options) {
    return null;
  }

  @Override
  public <V> RSet<V> getSet(final String name) {
    return null;
  }

  @Override
  public <V> RSet<V> getSet(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RSet<V> getSet(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RSortedSet<V> getSortedSet(final String name) {
    return null;
  }

  @Override
  public <V> RSortedSet<V> getSortedSet(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RSortedSet<V> getSortedSet(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RScoredSortedSet<V> getScoredSortedSet(final String name) {
    return null;
  }

  @Override
  public <V> RScoredSortedSet<V> getScoredSortedSet(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RScoredSortedSet<V> getScoredSortedSet(final PlainOptions options) {
    return null;
  }

  @Override
  public RLexSortedSet getLexSortedSet(final String name) {
    return null;
  }

  @Override
  public RLexSortedSet getLexSortedSet(final CommonOptions options) {
    return null;
  }

  @Override
  public RShardedTopic getShardedTopic(final String name) {
    return null;
  }

  @Override
  public RShardedTopic getShardedTopic(final String name, final Codec codec) {
    return null;
  }

  @Override
  public RShardedTopic getShardedTopic(final PlainOptions options) {
    return null;
  }

  @Override
  public RTopic getTopic(final String name) {
    return null;
  }

  @Override
  public RTopic getTopic(final String name, final Codec codec) {
    return null;
  }

  @Override
  public RTopic getTopic(final PlainOptions options) {
    return null;
  }

  @Override
  public RReliableTopic getReliableTopic(final String name) {
    return null;
  }

  @Override
  public RReliableTopic getReliableTopic(final String name, final Codec codec) {
    return null;
  }

  @Override
  public RReliableTopic getReliableTopic(final PlainOptions options) {
    return null;
  }

  @Override
  public RPatternTopic getPatternTopic(final String pattern) {
    return null;
  }

  @Override
  public RPatternTopic getPatternTopic(final String pattern, final Codec codec) {
    return null;
  }

  @Override
  public RPatternTopic getPatternTopic(final PatternTopicOptions options) {
    return null;
  }

  @Override
  public <V> RQueue<V> getQueue(final String name) {
    return null;
  }

  @Override
  public <V> RTransferQueue<V> getTransferQueue(final String name) {
    return null;
  }

  @Override
  public <V> RTransferQueue<V> getTransferQueue(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RTransferQueue<V> getTransferQueue(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RDelayedQueue<V> getDelayedQueue(final RQueue<V> destinationQueue) {
    return null;
  }

  @Override
  public <V> RReliableQueue<V> getReliableQueue(final String name) {
    return null;
  }

  @Override
  public <V> RReliableQueue<V> getReliableQueue(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RReliableQueue<V> getReliableQueue(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RQueue<V> getQueue(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RQueue<V> getQueue(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RRingBuffer<V> getRingBuffer(final String name) {
    return null;
  }

  @Override
  public <V> RRingBuffer<V> getRingBuffer(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RRingBuffer<V> getRingBuffer(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RPriorityQueue<V> getPriorityQueue(final String name) {
    return null;
  }

  @Override
  public <V> RPriorityQueue<V> getPriorityQueue(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RPriorityQueue<V> getPriorityQueue(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(final String name) {
    return null;
  }

  @Override
  public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RPriorityBlockingQueue<V> getPriorityBlockingQueue(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(final String name) {
    return null;
  }

  @Override
  public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RPriorityBlockingDeque<V> getPriorityBlockingDeque(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RPriorityDeque<V> getPriorityDeque(final String name) {
    return null;
  }

  @Override
  public <V> RPriorityDeque<V> getPriorityDeque(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RPriorityDeque<V> getPriorityDeque(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RBlockingQueue<V> getBlockingQueue(final String name) {
    return null;
  }

  @Override
  public <V> RBlockingQueue<V> getBlockingQueue(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RBlockingQueue<V> getBlockingQueue(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(final String name) {
    return null;
  }

  @Override
  public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RBoundedBlockingQueue<V> getBoundedBlockingQueue(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RDeque<V> getDeque(final String name) {
    return null;
  }

  @Override
  public <V> RDeque<V> getDeque(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RDeque<V> getDeque(final PlainOptions options) {
    return null;
  }

  @Override
  public <V> RBlockingDeque<V> getBlockingDeque(final String name) {
    return null;
  }

  @Override
  public <V> RBlockingDeque<V> getBlockingDeque(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RBlockingDeque<V> getBlockingDeque(final PlainOptions options) {
    return null;
  }

  @Override
  public RAtomicLong getAtomicLong(final String name) {
    return null;
  }

  @Override
  public RAtomicLong getAtomicLong(final CommonOptions options) {
    return null;
  }

  @Override
  public RAtomicDouble getAtomicDouble(final String name) {
    return null;
  }

  @Override
  public RAtomicDouble getAtomicDouble(final CommonOptions options) {
    return null;
  }

  @Override
  public RLongAdder getLongAdder(final String name) {
    return null;
  }

  @Override
  public RLongAdder getLongAdder(final CommonOptions options) {
    return null;
  }

  @Override
  public RDoubleAdder getDoubleAdder(final String name) {
    return null;
  }

  @Override
  public RDoubleAdder getDoubleAdder(final CommonOptions options) {
    return null;
  }

  @Override
  public RCountDownLatch getCountDownLatch(final String name) {
    return null;
  }

  @Override
  public RCountDownLatch getCountDownLatch(final CommonOptions options) {
    return null;
  }

  @Override
  public RBitSet getBitSet(final String name) {
    return null;
  }

  @Override
  public RBitSet getBitSet(final CommonOptions options) {
    return null;
  }

  @Override
  public <V> RBloomFilter<V> getBloomFilter(final String name) {
    return null;
  }

  @Override
  public <V> RBloomFilter<V> getBloomFilter(final String name, final Codec codec) {
    return null;
  }

  @Override
  public <V> RBloomFilter<V> getBloomFilter(final PlainOptions options) {
    return null;
  }

  @Override
  public RIdGenerator getIdGenerator(final String name) {
    return null;
  }

  @Override
  public RIdGenerator getIdGenerator(final CommonOptions options) {
    return null;
  }

  @Override
  public RFunction getFunction() {
    return null;
  }

  @Override
  public RFunction getFunction(final Codec codec) {
    return null;
  }

  @Override
  public RFunction getFunction(final OptionalOptions options) {
    return null;
  }

  @Override
  public RScript getScript() {
    return null;
  }

  @Override
  public RScript getScript(final Codec codec) {
    return null;
  }

  @Override
  public RScript getScript(final OptionalOptions options) {
    return null;
  }

  @Override
  public RVectorSet getVectorSet(final String name) {
    return null;
  }

  @Override
  public RVectorSet getVectorSet(final CommonOptions options) {
    return null;
  }

  @Override
  public RScheduledExecutorService getExecutorService(final String name) {
    return null;
  }

  @Override
  public RScheduledExecutorService getExecutorService(final String name, final ExecutorOptions options) {
    return null;
  }

  @Override
  public RScheduledExecutorService getExecutorService(final String name, final Codec codec) {
    return null;
  }

  @Override
  public RScheduledExecutorService getExecutorService(final String name, final Codec codec, final ExecutorOptions options) {
    return null;
  }

  @Override
  public RScheduledExecutorService getExecutorService(final org.redisson.api.options.ExecutorOptions options) {
    return null;
  }

  @Override
  public RRemoteService getRemoteService() {
    return null;
  }

  @Override
  public RRemoteService getRemoteService(final Codec codec) {
    return null;
  }

  @Override
  public RRemoteService getRemoteService(final String name) {
    return null;
  }

  @Override
  public RRemoteService getRemoteService(final String name, final Codec codec) {
    return null;
  }

  @Override
  public RRemoteService getRemoteService(final PlainOptions options) {
    return null;
  }

  @Override
  public RTransaction createTransaction(final TransactionOptions options) {
    return null;
  }

  @Override
  public RBatch createBatch(final BatchOptions options) {
    return null;
  }

  @Override
  public RBatch createBatch() {
    return null;
  }

  @Override
  public RKeys getKeys() {
    return null;
  }

  @Override
  public RKeys getKeys(final KeysOptions options) {
    return null;
  }

  @Override
  public RLiveObjectService getLiveObjectService() {
    return null;
  }

  @Override
  public RLiveObjectService getLiveObjectService(final LiveObjectOptions options) {
    return null;
  }

  @Override
  public RClientSideCaching getClientSideCaching(final ClientSideCachingOptions options) {
    return null;
  }

  @Override
  public RedissonRxClient rxJava() {
    return null;
  }

  @Override
  public RedissonReactiveClient reactive() {
    return null;
  }

  @Override
  public void shutdown() {

  }

  @Override
  public void shutdown(final long quietPeriod, final long timeout, final TimeUnit unit) {

  }

  @Override
  public Config getConfig() {
    return null;
  }

  @Override
  public <T extends BaseRedisNodes> T getRedisNodes(final RedisNodes<T> nodes) {
    return null;
  }

  @Override
  public NodesGroup<Node> getNodesGroup() {
    return null;
  }

  @Override
  public ClusterNodesGroup getClusterNodesGroup() {
    return null;
  }

  @Override
  public boolean isShutdown() {
    return false;
  }

  @Override
  public boolean isShuttingDown() {
    return false;
  }

  @Override
  public String getId() {
    return "";
  }
}
