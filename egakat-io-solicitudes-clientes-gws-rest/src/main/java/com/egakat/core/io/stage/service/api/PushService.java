package com.egakat.core.io.stage.service.api;

import com.egakat.core.web.client.service.api.CacheEvictSupported;

public interface PushService extends CacheEvictSupported {
	void push();
}