<script lang="ts">
	import { resolve } from '$app/paths';
	import { page } from '$app/state';
	import { Clock, Captions, Compass, Server } from '@lucide/svelte';

	const errorData = extractErrorObject();

	const status = page.status;
	const errorMessage = errorData?.error || page.error?.message;
	const backendMessage = errorData?.message;

	const timestamp = errorData?.timestamp ?? new Date().toISOString();
	const frontendPath = page.url.pathname;
	const backendPath = errorData?.path;
	const trace = errorData?.trace?.split('\n').slice(0, 10).join('\n');

	console.log(errorData);
	function extractErrorObject() {
		const message = page.error?.message ?? '{}';
		try {
			return JSON.parse(message);
		} catch (_) {
			return undefined;
		}
	}
</script>

<div class="flex h-full w-full flex-col p-16">
	<div class="prose min-w-full">
		<h1 class="mb-2">
			{status}
			{errorMessage}
		</h1>
		<div class="mt-0 w-full font-bold">
			<div class="badge badge-ghost"><Clock />{timestamp}</div>

			{#if backendPath}
				<div class="badge badge-ghost">
					<Server />{backendPath}
				</div>
			{/if}
			<div class="badge badge-ghost">
				<Compass />
				{frontendPath}
			</div>
		</div>

		{#if backendMessage}
			<h2 class="mb-2">{backendMessage}</h2>
		{/if}
		{#if trace}
			<div class="mockup-code mt-4 w-full bg-warning text-warning-content">
				<pre data-prefix="$" class="bg-warning text-warning-content"><code>{trace}</code></pre>
			</div>
		{/if}
		<h2 class="mb-1">This was not supposed to happen</h2>
		<p class="italic">
			You can go back to the <a href={resolve('/')}> home page </a>
		</p>
	</div>
</div>
