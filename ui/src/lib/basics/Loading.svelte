<script lang="ts">
	import type { HTMLAttributes } from 'svelte/elements';

	let { class: className, ...rest }: HTMLAttributes<HTMLSpanElement> = $props();
</script>

<span role="status" aria-label="Loading..." class="loading loading-ring {className}" {...rest}>
</span>
