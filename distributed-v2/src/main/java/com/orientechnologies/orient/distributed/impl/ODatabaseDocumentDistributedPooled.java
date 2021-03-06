package com.orientechnologies.orient.distributed.impl;

import com.orientechnologies.orient.core.db.ODatabaseDocumentInternal;
import com.orientechnologies.orient.core.db.ODatabasePoolInternal;
import com.orientechnologies.orient.core.db.config.ONodeIdentity;
import com.orientechnologies.orient.core.storage.OStorage;
import com.orientechnologies.orient.distributed.hazelcast.OHazelcastPlugin;

/**
 * Created by tglman on 30/03/17.
 */
public class ODatabaseDocumentDistributedPooled extends ODatabaseDocumentDistributed {

  private ODatabasePoolInternal pool;

  public ODatabaseDocumentDistributedPooled(ODatabasePoolInternal pool, OStorage storage, ONodeIdentity nodeIdentity) {
    super(storage, nodeIdentity);
    this.pool = pool;
  }

  @Override
  public void close() {
    if (isClosed())
      return;
    internalClose(true);
    pool.release(this);
  }

  @Override
  public ODatabaseDocumentInternal copy() {
    return (ODatabaseDocumentInternal) pool.acquire();
  }

  public void reuse() {
    activateOnCurrentThread();
    setStatus(STATUS.OPEN);
  }

  public void realClose() {
    activateOnCurrentThread();
    super.close();
  }
}
