<script lang="ts">
	import SessionBottomBar from './SessionBottomBar.svelte';
	import TimerBar from './TimerBar.svelte';
</script>

<div class="flex w-full flex-1 flex-col items-center justify-between gap-6">
	<div>header</div>
	<div class="flex-1">content</div>
	<TimerBar />

	<SessionBottomBar />
</div>
