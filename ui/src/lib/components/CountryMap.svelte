<script lang="ts">
	interface Props {
		countryCode: string;
		markerLon?: number;
		markerLat?: number;
		showNeighbors?: boolean;
	}

	let { countryCode, markerLon, markerLat, showNeighbors = true }: Props = $props();

	interface CountryData {
		name: string;
		path: string;
		provinces: string[];
		neighbors: string[];
	}

	let country: CountryData | null = $state(null);
	let neighborPaths: string[] = $state([]);

	async function loadCountry(code: string): Promise<CountryData | null> {
		const res = await fetch(`/countries/${code}.json`);
		if (!res.ok) return null;
		return (await res.json()) as CountryData;
	}

	// Load country + neighbors when countryCode changes
	$effect(() => {
		const code = countryCode;
		country = null;
		neighborPaths = [];

		loadCountry(code).then(async (data) => {
			if (!data) return;
			country = data;

			if (showNeighbors) {
				const paths = await Promise.all(
					data.neighbors.map(async (n) => {
						const nd = await loadCountry(n);
						return nd?.path ?? null;
					})
				);
				neighborPaths = paths.filter((p): p is string => !!p);
			}
		});
	});

	// Get bounding box of a path string
	function pathBBox(p: string) {
		const xs: number[] = [];
		const ys: number[] = [];
		for (const m of p.matchAll(/([\d.]+),([\d.]+)/g)) {
			xs.push(+m[1]);
			ys.push(+m[2]);
		}
		return { minX: Math.min(...xs), minY: Math.min(...ys), maxX: Math.max(...xs), maxY: Math.max(...ys) };
	}

	// Compute viewBox: largest polygon + nearby polygons (excludes distant territories)
	const viewBox = $derived.by(() => {
		if (!country) return '0 0 1000 1000';

		// Split into sub-paths
		const subPaths = country.path.split(/(?=M)/).filter(Boolean);

		// Find largest sub-path
		let largestIdx = 0;
		let maxPoints = 0;
		for (let i = 0; i < subPaths.length; i++) {
			const count = [...subPaths[i].matchAll(/([\d.]+),([\d.]+)/g)].length;
			if (count > maxPoints) {
				maxPoints = count;
				largestIdx = i;
			}
		}

		// Get bbox of largest, expand it, include nearby sub-paths
		const mainBBox = pathBBox(subPaths[largestIdx]);
		const w = mainBBox.maxX - mainBBox.minX;
		const h = mainBBox.maxY - mainBBox.minY;
		const expanded = {
			minX: mainBBox.minX - w, minY: mainBBox.minY - h,
			maxX: mainBBox.maxX + w, maxY: mainBBox.maxY + h
		};

		const xs: number[] = [];
		const ys: number[] = [];
		for (const sp of subPaths) {
			const bb = pathBBox(sp);
			// Include if it overlaps with expanded main bbox
			if (bb.maxX >= expanded.minX && bb.minX <= expanded.maxX &&
				bb.maxY >= expanded.minY && bb.minY <= expanded.maxY) {
				for (const m of sp.matchAll(/([\d.]+),([\d.]+)/g)) {
					xs.push(+m[1]);
					ys.push(+m[2]);
				}
			}
		}

		const minX = Math.min(...xs);
		const minY = Math.min(...ys);
		const maxX = Math.max(...xs);
		const maxY = Math.max(...ys);
		const pad = Math.max(maxX - minX, maxY - minY) * 0.05;
		return `${minX - pad} ${minY - pad} ${maxX - minX + pad * 2} ${maxY - minY + pad * 2}`;
	});

	// Project marker to same Mercator as the generation script
	const marker = $derived.by(() => {
		if (markerLon == null || markerLat == null) return null;
		const x = ((markerLon + 180) / 360) * 1000;
		const latRad = (Math.max(-85, Math.min(85, markerLat)) * Math.PI) / 180;
		const y = (0.5 - Math.log(Math.tan(Math.PI / 4 + latRad / 2)) / (2 * Math.PI)) * 1000;
		return { x: Math.round(x * 100) / 100, y: Math.round(y * 100) / 100 };
	});

	const markerRadius = $derived.by(() => {
		const parts = viewBox.split(' ').map(Number);
		return Math.max(parts[2], parts[3]) * 0.02;
	});

	const strokeWidth = $derived.by(() => {
		const parts = viewBox.split(' ').map(Number);
		return Math.max(parts[2], parts[3]) * 0.003;
	});
</script>

{#if country}
	<svg viewBox={viewBox} xmlns="http://www.w3.org/2000/svg" class="h-full w-full">
		<!-- Neighbor countries (faint) -->
		{#if showNeighbors}
			{#each neighborPaths as np}
				<path d={np} class="fill-base-300/50 stroke-base-300" stroke-width={strokeWidth} />
			{/each}
		{/if}

		<!-- Main country fill -->
		<path d={country.path} class="fill-primary/20 stroke-primary" stroke-width={strokeWidth} />

		<!-- Province borders -->
		{#each country.provinces as prov}
			<path d={prov} fill="none" class="stroke-primary/30" stroke-width={strokeWidth * 0.5} />
		{/each}

		<!-- Marker -->
		{#if marker}
			<circle cx={marker.x} cy={marker.y} r={markerRadius} class="fill-accent" />
		{/if}
	</svg>
{/if}