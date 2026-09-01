<script lang="ts">
	import { onMount } from 'svelte';
	import type { TeaDTO, TeaTypeDto } from '$lib/api/gen/types';
	import { goto, invalidateAll } from '$app/navigation';
	import { resolve } from '$app/paths';
	import Select from '$lib/basics/Select.svelte';
	import Combobox from '$lib/basics/Combobox.svelte';
	import { icons } from '$lib/basics/icons';
	import Input from '$lib/basics/Input.svelte';
	import Markdown from '$lib/basics/Markdown.svelte';
	import Textarea from '$lib/basics/Textarea.svelte';
	import Button from '$lib/basics/Button.svelte';
	import { ArrowDownToLine } from '@lucide/svelte';
	import { teaService } from '$lib/api/tea.service';
	import { teaTypeService } from '$lib/api/teaType.service';
	import { cultivarService } from '$lib/api/cultivar.service';
	import { vendorService } from '$lib/api/vendor.service';
	import { extractionService } from '$lib/api/extraction.service';
	import { toast } from '$lib/toast/toast.store.svelte';

	let {
		form = $bindable({
			name: '',
			cultivar: '',
			teaType: '',
			vendor: '',
			harvestYear: undefined,
			harvestLabel: undefined,
			descriptionMd: '',
			originCountry: '',
			originProvince: '',
			originCity: '',
			price: undefined,
			purchaseDate: '',
			weightGrams: undefined
		})
	}: { form: TeaDTO } = $props();

	let editing = $derived<boolean>(form.id !== undefined);

	let teaTypes = $state<TeaTypeDto[]>([]);
	let countryNames = $state<string[]>([]);
	let error = $state('');

	let submitting = $state(false);
	let loading = $state(false);

	onMount(async () => {
		teaTypes = await teaTypeService.getAll();

		// Todo: turn into simple import instead of fetch
		fetch('/countries/index.json').then(async (res) => {
			if (res.ok) {
				const entries: { code: string; name: string }[] = await res.json();
				countryNames = entries.map((e) => e.name);
			}
		});
	});

	async function searchCultivars(q: string): Promise<string[]> {
		const data = await cultivarService.getAll(q);
		return data.map((c) => c.name ?? '');
	}

	async function searchVendors(q: string): Promise<string[]> {
		const data = await vendorService.getAll(q);
		return data.map((v) => v.vendor.name ?? '');
	}

	async function submit() {
		if (form.originCountry && !countryNames.includes(form.originCountry)) {
			error = 'Choose a valid country.';
			return;
		}
		submitting = true;

		let tea = await save().catch(() => undefined);

		submitting = false;
		if (!tea) return;

		let id = form.id ?? tea?.id;

		toast.success(`Successfully ${editing ? 'updated' : 'created'} '${tea.name}'`);

		await invalidateAll();
		await goto(backlink(id));
	}

	function backlink(id: number | undefined) {
		if (id) {
			return resolve('/teas/[slug]', { slug: id + '' });
		}
		return resolve('/teas');
	}

	function save() {
		if (editing) {
			return teaService.update(form);
		}
		return teaService.create(form);
	}

	async function scrapeUrl() {
		if (!form.website) return;
		loading = true;

		let result = await extractionService.extract(form.website).catch(() => undefined);

		loading = false;
		if (!result || !result.teaDTO) return;

		for (const k in result.teaDTO) {
			const key = k as keyof TeaDTO;
			assignIfEmpty(result.teaDTO, form, key);
		}
	}

	function assignIfEmpty<T, K extends keyof T>(source: T, target: T, key: K) {
		if (source[key] && !target[key]) {
			target[key] = source[key];
		}
	}
</script>

<h1 class="text-3xl font-bold text-base-content">
	{editing ? 'Updating a tea' : 'Adding a new tea'}
</h1>

{#if !editing}
	<form
		class="mt-6 w-full space-y-4 md:w-60/100"
		onsubmit={(e) => {
			e.preventDefault();
			scrapeUrl();
		}}
	>
		<div class="form-control flex gap-2">
			<div class="flex-2">
				<Input
					disabled={loading || submitting}
					hint="Invalid Website"
					inputClass="w-full"
					placeholder="Website"
					type="url"
					bind:value={form.website}
				/>
			</div>
			<div>
				<Button
					class="btn-primary"
					disabled={!form.website || submitting}
					icon={ArrowDownToLine}
					label="Fetch Data"
					{loading}
					type="submit"
				/>
			</div>
		</div>

		<div class="divider"></div>
	</form>
{/if}

<form
	class="mt-6 w-full space-y-4 md:w-60/100"
	onsubmit={(e) => {
		e.preventDefault();
		submit();
	}}
>
	<div class="form-control">
		<Input
			disabled={loading || submitting}
			hint="Name is required"
			inputClass="w-full"
			placeholder="Name*"
			required
			bind:value={form.name}
		/>
	</div>

	<div class="form-control">
		<Select
			disabled={loading || submitting}
			label="Tea Type"
			options={teaTypes.map((t) => ({ value: t.name, label: t.name }))}
			prompt="Choose a tea type"
			bind:value={form.teaType}
		/>
	</div>

	<div class="form-control">
		<Combobox
			disabled={loading || submitting}
			icon={icons.cultivar}
			options={[]}
			placeholder="Cultivar z.B. Da Bai"
			search={searchCultivars}
			bind:value={form.cultivar}
		/>
	</div>

	<div class="form-control">
		<Combobox
			disabled={loading || submitting}
			icon={icons.vendor}
			options={[]}
			placeholder="Vendor"
			search={searchVendors}
			bind:value={form.vendor!}
		/>
	</div>

	{#if editing}
		<div class="form-control">
			<Input
				disabled={loading || submitting}
				hint="Invalid Website"
				inputClass="w-full"
				placeholder="Website"
				type="url"
				bind:value={form.website}
			/>
		</div>
	{/if}

	<div class="form-control">
		<div class="grid grid-cols-2 gap-4">
			<Input
				disabled={loading || submitting}
				hint="Cant be in the future"
				inputClass="w-full"
				max={new Date().getFullYear()}
				min={0}
				placeholder="Harvest Year"
				step="1"
				type="number"
				bind:value={form.harvestYear}
			/>

			<Input
				disabled={loading || submitting}
				inputClass="w-full"
				placeholder="Harvest Label"
				bind:value={form.harvestLabel}
			/>
		</div>
	</div>

	<div class="form-control">
		<div class="grid grid-cols-2 gap-4">
			<Textarea
				disabled={loading || submitting}
				placeholder="Description"
				bind:value={form.descriptionMd}
			/>

			<Markdown class="overflow-auto" md={form.descriptionMd}>
				{#snippet before()}
					<h3>Preview</h3>
				{/snippet}
			</Markdown>
		</div>
	</div>

	<fieldset class="fieldset">
		<legend class="fieldset-legend flex items-start">Origin</legend>
		<div class="grid grid-cols-3 gap-4">
			<div class="form-control">
				<Combobox
					disabled={loading || submitting}
					options={countryNames}
					placeholder="Country"
					bind:value={form.originCountry!}
				/>
			</div>
			<div class="form-control">
				<Input
					disabled={loading || submitting}
					inputClass=" w-full"
					placeholder="Province"
					bind:value={form.originProvince}
				/>
			</div>
			<div class="form-control">
				<Input
					disabled={loading || submitting}
					inputClass="w-full"
					placeholder="City"
					bind:value={form.originCity}
				/>
			</div>
		</div>
	</fieldset>

	<fieldset class="fieldset">
		<legend class="fieldset-legend flex items-start">Purchase</legend>
		<div class="grid grid-cols-3 gap-4">
			<div class="form-control">
				<Input
					disabled={loading || submitting}
					hint="Must be at least 0"
					inputClass="w-full"
					min={0}
					placeholder="Price"
					step="0.01"
					type="number"
					bind:value={form.price}
				/>
			</div>
			<div class="form-control">
				<Input
					disabled={loading || submitting}
					hint="Must be at least 0"
					inputClass="w-full"
					min={0}
					placeholder="Weight"
					step="0.01"
					type="number"
					bind:value={form.weightGrams}
				/>
			</div>
			<div class="form-control">
				<Input
					disabled={loading || submitting}
					inputClass="w-full"
					placeholder="Date of Purchase"
					type="date"
					bind:value={form.purchaseDate}
				/>
			</div>
		</div>
	</fieldset>

	{#if error}
		<div class="alert alert-error">{error}</div>
	{/if}

	<div class="flex gap-2">
		<Button
			class="btn-primary"
			disabled={loading}
			label={submitting ? 'Saving...' : editing ? 'Update' : 'Create'}
			loading={submitting}
			type="submit"
		/>
		<a class="btn btn-ghost" href={backlink(form.id)}>Cancel</a>
	</div>
</form>
