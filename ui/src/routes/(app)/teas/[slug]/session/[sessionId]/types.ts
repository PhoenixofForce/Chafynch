export type TastingNoteCategory = {
	name: string;
	subCategories: string[];
};

export const categories: TastingNoteCategory[] = [
	{ name: 'Eye', subCategories: ['Wet Leaf', 'Liquor'] },
	{ name: 'Nose', subCategories: ['Wet Leaf', 'Liquor', 'Empty Cup'] },
	{ name: 'Mouth', subCategories: ['Texture', 'Taste', 'Finish'] }
];

export const globalCategories: TastingNoteCategory[] = [
	{ name: 'Eye', subCategories: ['Dry Leaf'] },
	{ name: 'Nose', subCategories: ['Dry Leaf'] }
];

export type Tabs =
	| {
			tab: 'infusion';
			index: number;
	  }
	| {
			tab: 'start';
	  }
	| {
			tab: 'end';
	  };
